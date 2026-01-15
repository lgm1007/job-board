package org.lgm.jobboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JobBoardApplication

fun main(args: Array<String>) {
	runApplication<JobBoardApplication>(*args)
}
