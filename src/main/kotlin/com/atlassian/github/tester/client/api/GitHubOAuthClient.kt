package com.atlassian.github.tester.client.api

import retrofit2.Call
import retrofit2.http.*

interface GitHubOAuthClient {

    @POST("/login/oauth/access_token")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessTokenResponse>

}