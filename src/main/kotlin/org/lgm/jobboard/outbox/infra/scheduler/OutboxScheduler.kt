package org.lgm.jobboard.outbox.infra.scheduler

import org.lgm.jobboard.outbox.application.service.OutboxPollingService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxScheduler(
	private val pollingService: OutboxPollingService
) {
	@Scheduled(fixedDelayString = "\${app.outbox.poll-interval-ms:1000}")
	fun poll() {
		pollingService.pollOnce(batchSize = 50)
	}
}