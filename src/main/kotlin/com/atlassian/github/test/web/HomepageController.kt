package com.atlassian.github.test.web

import com.atlassian.github.test.UserTokenHolder
import com.atlassian.github.test.client.GitHubOAuthClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class HomepageController(
    @Value("\${github.client-id}") private val clientId: String,
    @Value("\${github.client-secret}") private val clientSecret: String,
    private val gitHubOAuthClient: GitHubOAuthClient,
    private val userTokenHolder: UserTokenHolder,
    private val testRunner: TestRunner
) {

    @GetMapping("/")
    fun render(): ModelAndView {
        return ModelAndView(
            "/home.html", mapOf(
                Pair(
                    "oauthUrl",
                    "https://github.com/login/oauth/authorize?client_id=${clientId}&state=${UUID.randomUUID()}"
                )
            )
        )
    }

    @GetMapping("/callback")
    fun callback(@RequestParam("code") code: String): ModelAndView {
        // a proper callback should verify the "state" parameter ... we're not doing that here
        val accessTokenResponse = gitHubOAuthClient.getAccessToken(clientId, clientSecret, code).execute().body()

        userTokenHolder.set(
            accessTokenResponse?.accessToken
                ?: throw java.lang.RuntimeException("didn't receive an access token from GitHub")
        )

        val testCaseResults = testRunner.runTests()

        return ModelAndView(
            "/callback.html", mapOf(Pair("testCaseResults", testCaseResults))
        )
    }

}