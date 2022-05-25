package com.atlassian.github.tester.web

import com.atlassian.github.tester.client.factory.GitHubInstallationClientFactory
import com.atlassian.github.tester.client.factory.GitHubUserClientFactory
import com.atlassian.github.tester.client.factory.GithubAppClientFactory
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class TestRunner(
    private val gitHubUserClientFactory: GitHubUserClientFactory,
    private val githubAppClientFactory: GithubAppClientFactory,
    private val gitHubInstallationClientFactory: GitHubInstallationClientFactory
) {

    fun runTests(): List<TestcaseResult> {

        val gitHubUserClient = gitHubUserClientFactory.githubUserClient()
        val gitHubAppClient = githubAppClientFactory.githubAppClient()

        val testCases = mutableListOf(
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

        val installationsResponse = gitHubAppClient.getInstallations().execute()
        if (!installationsResponse.isSuccessful) {
            throw java.lang.RuntimeException("could not load installations!")
        }

        for (installation in installationsResponse.body()!!) {
            testCases.add(
                Testcase(
                    AuthenticationMethod.INSTALLATION_TOKEN,
                    "https://api.github.com/installation/repositories (installation id: ${installation.id})",
                    executor = {
                        val client = gitHubInstallationClientFactory.githubInstallationClient(installation.id)
                        toTestResult(client.getRepositories().execute())
                    }
                )
            )
        }


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
            TestResult(
                ResultStatus.ERROR,
                "Error! HTTP status ${response.code()}. Message: ${response.errorBody()?.string()}"
            )
        }
    }

}