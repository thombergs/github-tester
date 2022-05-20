package com.atlassian.github.test

import com.atlassian.github.test.client.GitHubAppClient
import com.atlassian.github.test.client.GitHubOAuthClient
import com.atlassian.github.test.client.GitHubUserClient
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class GitHubConfiguration(
    private val appTokenHolder: AppTokenHolder,
    private val objectMapper: ObjectMapper,
    private val userTokenHolder: UserTokenHolder
) {

    private val httpLogger = LoggerFactory.getLogger("OkHttp")

    @Bean
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

    @Bean
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

    @Bean
    fun githubOAuthClient(): GitHubOAuthClient {
        val client = baseClientBuilder()
            .build()

        return Retrofit.Builder().client(client)
            .baseUrl("https://github.com")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(GitHubOAuthClient::class.java)
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

    private fun baseClientBuilder(): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                httpLogger.info(message)
            }

        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
    }

}