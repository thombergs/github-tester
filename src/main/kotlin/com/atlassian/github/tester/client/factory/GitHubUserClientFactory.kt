package com.atlassian.github.tester.client.factory

import com.atlassian.github.tester.client.api.GitHubUserClient
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Component
class GitHubUserClientFactory(
    private val objectMapper: ObjectMapper,
    private val userTokenHolder: UserTokenHolder,
): GitHubClientFactory() {

    fun githubUserClient(): GitHubUserClient {
        val client = baseClientBuilder()
            .addInterceptor(asUserHeaderInterceptor())
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GitHubUserClient::class.java)
    }

    private fun asUserHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestWithHeaders = request.newBuilder()
                .header("Authorization", "token ${userTokenHolder.get()}")
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            chain.proceed(requestWithHeaders)
        }
    }

}