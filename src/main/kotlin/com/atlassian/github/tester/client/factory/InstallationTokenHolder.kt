package com.atlassian.github.tester.client.factory

import org.springframework.stereotype.Component


@Component
class InstallationTokenHolder(
    private val githubAppClientFactory: GithubAppClientFactory
) {

    private val installationTokens = mutableMapOf<Int, ExpirableToken>()

    /**
     * Returns the cached installation token or generates a new one if it doesn't exist or is about to expire.
     */
    fun getInstallationToken(installationId: Int): ExpirableToken {
        val existingToken = installationTokens[installationId]

        if (existingToken != null && !existingToken.isAboutToExpire()) {
            return existingToken;
        }

        val token = generateToken(installationId)

        installationTokens[installationId] = token

        return token
    }

    private fun generateToken(installationId: Int): ExpirableToken {
        val githubAppClient = githubAppClientFactory.githubAppClient()

        val response = githubAppClient.getInstallation(installationId).execute()

        if (!response.isSuccessful || response.body() == null) {
            throw java.lang.RuntimeException("Could not find installation with ID $installationId")
        }

        val installationTokenResponse = githubAppClient.getInstallationToken(installationId).execute()

        if (!installationTokenResponse.isSuccessful || installationTokenResponse.body() == null) {
            throw java.lang.RuntimeException("Could not get installation token for installation $installationId!")
        }

        val token = installationTokenResponse.body()!!

        return ExpirableToken(token.token, token.expiresAt)
    }

}