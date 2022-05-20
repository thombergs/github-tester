package com.atlassian.github.test.client

import retrofit2.Call
import retrofit2.http.GET

interface GitHubInstallationClient {

    @GET("/installation/repositories")
    fun getRepositories(): Call<UserInstallationsResponse>

}