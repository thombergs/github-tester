package com.atlassian.github.tester.client.factory

import com.atlassian.github.tester.client.api.GitHubOAuthClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Component
class GitHubOAuthClientFactory(
    private val objectMapper: ObjectMapper,
): GitHubClientFactory() {

    fun githubOAuthClient(): GitHubOAuthClient {
        val client = baseClientBuilder()
            .build()

        return Retrofit.Builder().client(client)
            .baseUrl("https://github.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GitHubOAuthClient::class.java)
    }

}