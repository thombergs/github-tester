package com.atlassian.github.tester.client.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubAppClient {

    @GET("/app/installations")
    fun getInstallations(): Call<List<Installation>>

    @POST("app/installations/{id}/access_tokens")
    fun getInstallationToken(@Path("id") installationId: Int): Call<InstallationTokenResponse>

    @GET("/app/installations/{id}")
    fun getInstallation(@Path("id") installationId: Int): Call<Installation>

}