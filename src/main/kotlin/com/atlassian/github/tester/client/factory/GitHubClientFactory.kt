package com.atlassian.github.tester.client.factory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory

abstract class GitHubClientFactory {

    private val httpLogger = LoggerFactory.getLogger("OkHttp")

    protected fun baseClientBuilder(): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                httpLogger.info(message)
            }

        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
    }

}