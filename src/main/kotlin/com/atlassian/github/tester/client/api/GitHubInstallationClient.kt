package com.atlassian.github.tester.client.api

import retrofit2.Call
import retrofit2.http.GET

interface GitHubInstallationClient {

    @GET("/installation/repositories")
    fun getRepositories(): Call<GetReposResponse>

}