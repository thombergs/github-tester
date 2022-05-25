package com.atlassian.github.tester.client.factory

import com.atlassian.github.tester.client.api.GitHubAppClient
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Component
class GithubAppClientFactory(
    private val appTokenHolder: AppTokenHolder,
    private val objectMapper: ObjectMapper,
): GitHubClientFactory() {

    fun githubAppClient(): GitHubAppClient {
        val client = baseClientBuilder()
            .addInterceptor(asAppHeaderInterceptor())
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GitHubAppClient::class.java)
    }

    private fun asAppHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestWithHeaders = request.newBuilder()
                .header("Authorization", "Bearer ${appTokenHolder.getAppToken().token}")
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            chain.proceed(requestWithHeaders)
        }
    }

}