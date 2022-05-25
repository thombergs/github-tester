package com.atlassian.github.tester.client.api

import com.fasterxml.jackson.annotation.JsonProperty

data class GetReposResponse(
    @JsonProperty("total_count")
    val totalCount: Int
)