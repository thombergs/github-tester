package com.atlassian.github.test

import java.time.Instant
import java.util.Date

data class ExpirableToken(
    val token: String,
    val expirationDate: Date
) {
    fun isAboutToExpire(): Boolean {
        return Date.from(Instant.now().plusSeconds(60)).after(this.expirationDate)
    }
}