package com.atlassian.github.test

import org.springframework.stereotype.Component

@Component
class UserTokenHolder {
    private var accessToken = ""

    fun set(accessToken: String) {
        this.accessToken = accessToken
    }

    fun get(): String {
        return this.accessToken
    }
}