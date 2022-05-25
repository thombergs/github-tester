package com.atlassian.github.tester.client.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class InstallationTokenResponse(
    @JsonProperty("token")
    val token: String,

    @JsonProperty("expires_at")
    val expiresAt: Date
)
