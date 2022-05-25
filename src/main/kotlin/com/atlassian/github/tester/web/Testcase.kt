package com.atlassian.github.tester.web

enum class AuthenticationMethod{
    USER_TOKEN,
    APP_TOKEN,
    INSTALLATION_TOKEN
}

data class TestResult(
    val status: ResultStatus,
    val message: String
)

enum class ResultStatus{
    SUCCESS,
    ERROR
}

data class Testcase(
    val authenticationMethod: AuthenticationMethod,
    val requestUrl: String,
    val executor: () -> TestResult
)

data class TestcaseResult(
    val authenticationMethod: AuthenticationMethod,
    val requestUrl: String,
    val result: TestResult
)