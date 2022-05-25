package com.atlassian.github.tester.client.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Installation(
    @JsonProperty("id")
    val id: Int
)
