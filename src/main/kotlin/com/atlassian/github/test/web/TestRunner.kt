package com.atlassian.github.test.web

import com.atlassian.github.test.client.GitHubAppClient
import com.atlassian.github.test.client.GitHubUserClient
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class TestRunner(
    private val gitHubUserClient: GitHubUserClient,
    private val gitHubAppClient: GitHubAppClient
) {

    fun runTests(): List<TestcaseResult> {
        val testCases = listOf(
            Testcase(
                AuthenticationMethod.USER_TOKEN,
                "https://api.github.com/user/installations",
                executor = { toTestResult(gitHubUserClient.getInstallations().execute()) }
            ),
            Testcase(
                AuthenticationMethod.APP_TOKEN,
                "https://api.github.com/app/installations",
                executor = { toTestResult(gitHubAppClient.getInstallations().execute()) }
            )
        )

        val testCaseResults = mutableListOf<TestcaseResult>()

        testCases.forEach { testCase ->
            testCaseResults.add(
                TestcaseResult(
                    testCase.authenticationMethod,
                    testCase.requestUrl,
                    testCase.executor.invoke()
                )
            )
        }

        return testCaseResults
    }

    private fun toTestResult(response: Response<out Any>): TestResult {
        return if (response.isSuccessful) {
            TestResult(ResultStatus.SUCCESS, "HTTP status ${response.code()}")
        } else {
            TestResult(ResultStatus.ERROR, "Error! HTTP status ${response.code()}. Message: ${response.errorBody()?.string()}")
        }
    }

}