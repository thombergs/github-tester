package com.atlassian.github.test.client

import retrofit2.Call
import retrofit2.http.GET

interface GitHubAppClient {

    @GET("/app/installations")
    fun getInstallations(): Call<List<Installation>>

}