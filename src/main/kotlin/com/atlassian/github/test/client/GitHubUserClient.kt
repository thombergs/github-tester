package com.atlassian.github.test.client

import retrofit2.Call
import retrofit2.http.GET

interface GitHubUserClient {

    @GET("/user/installations")
    fun getInstallations(): Call<UserInstallationsResponse>

}