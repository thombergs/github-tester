package com.atlassian.github.tester

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.Security

@SpringBootApplication
class GitHubTesterApplication

fun main(args: Array<String>) {
	Security.addProvider(BouncyCastleProvider())
	runApplication<GitHubTesterApplication>(*args)
}
