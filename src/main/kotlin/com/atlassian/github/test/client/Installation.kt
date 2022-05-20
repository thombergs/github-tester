package com.atlassian.github.test.client

import com.fasterxml.jackson.annotation.JsonProperty

data class Installation(
    @JsonProperty("id")
    val id: Int
)
