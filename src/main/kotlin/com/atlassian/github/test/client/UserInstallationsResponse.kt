package com.atlassian.github.test.client

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInstallationsResponse(
    @JsonProperty("total_count")
    val totalCount: Int,
    @JsonProperty("installations")
    val installations: List<UserInstallation>
)

data class UserInstallation(
    @JsonProperty("id")
    val id: Int
)