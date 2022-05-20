package com.atlassian.github.test

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.FileReader
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.time.Instant
import java.util.*


@Component
class AppTokenHolder(
    @Value("\${github.private-key-file}")
    private val privateKeyPath: String,
    @Value("\${github.app-id}")
    private val appId: String
) {

    private val logger = LoggerFactory.getLogger(AppTokenHolder::class.java)
    private var appToken: ExpirableToken = generateAppToken()

    /**
     * Returns the app token. Generates a new token if the current token is about to expire.
     */
    fun getAppToken(): ExpirableToken {
        if (this.appToken.isAboutToExpire()) {
            this.appToken = generateAppToken()
        }
        return this.appToken
    }

    /**
     * Generates an app token from the GitHub app's private key.
     */
    private fun generateAppToken(): ExpirableToken {
        val algorithm = Algorithm.RSA256(null, getPrivateKey())

        val expirationDate = Date.from(Instant.now().plusSeconds(60 * 10))

        val token = JWT.create()
            .withIssuer(appId)
            .withIssuedAt(Date.from(Instant.now().minusSeconds(60)))
            .withExpiresAt(expirationDate)
            .sign(algorithm)

        logger.info("successfully generated app token from private key in $privateKeyPath")

        return ExpirableToken(token, expirationDate)
    }

    private fun getPrivateKey(): RSAPrivateKey {
        val pemParser = PEMParser(FileReader(privateKeyPath))
        val converter = JcaPEMKeyConverter().setProvider("BC")
        val pemObject = pemParser.readObject()
        val keyPair = converter.getKeyPair(pemObject as PEMKeyPair)
        val privateKey: PrivateKey = keyPair.private
        return privateKey as RSAPrivateKey
    }

}