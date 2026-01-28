package org.lgm.jobboard.outbox.infra.persistence.adapter

import org.lgm.jobboard.outbox.application.port.OutboxQueryPort
import org.lgm.jobboard.outbox.application.query.OutboxEventView
import org.lgm.jobboard.outbox.infra.persistence.OutboxEventRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class OutboxQueryAdapter(
	private val outboxEventRepository: OutboxEventRepository
) : OutboxQueryPort {
	override fun findDueEventIds(
		now: OffsetDateTime,
		limit: Int
	): List<Long> {
		val pageable = PageRequest.of(0, limit.coerceIn(1, 200))
		return outboxEventRepository.findDueIds(now, pageable)
	}

	override fun lockById(id: Long): OutboxEventView? {
		val event = outboxEventRepository.lockById(id) ?: return null
		return OutboxEventView(
			id = event.id!!,
			aggregateType = event.aggregateType.name,
			aggregateId = event.aggregateId,
			eventType = event.eventType.name,
			payloadJson = event.payload,
			status = event.status.name,
			attempts = event.attempts,
			availableAt = event.availableAt
		)
	}
}