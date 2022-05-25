package com.atlassian.github.tester.client.factory

import com.atlassian.github.tester.client.api.GitHubInstallationClient
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Component
class GitHubInstallationClientFactory(
    private val objectMapper: ObjectMapper,
    private val installationTokenHolder: InstallationTokenHolder
): GitHubClientFactory() {

    fun githubInstallationClient(installationId: Int): GitHubInstallationClient {
        val client = baseClientBuilder()
            .addInterceptor(asInstallationHeaderInterceptor(installationId))
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GitHubInstallationClient::class.java)
    }

    private fun asInstallationHeaderInterceptor(installationId: Int): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestWithHeaders = request.newBuilder()
                .header("Authorization", "token ${installationTokenHolder.getInstallationToken(installationId).token}")
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            println("installation token: ${installationTokenHolder.getInstallationToken(installationId)}")
            chain.proceed(requestWithHeaders)
        }
    }

}