package org.lgm.jobboard.outbox.infra.persistence.adapter

import org.lgm.jobboard.outbox.application.port.OutboxCommandPort
import org.lgm.jobboard.outbox.domain.AggregateType
import org.lgm.jobboard.outbox.domain.EventType
import org.lgm.jobboard.outbox.infra.persistence.OutboxEventEntity
import org.lgm.jobboard.outbox.infra.persistence.OutboxEventRepository
import org.springframework.stereotype.Component

@Component
class OutboxCommandAdapter(
	private val outboxEventRepository: OutboxEventRepository
) : OutboxCommandPort {
	override fun enqueue(
		aggregateType: AggregateType,
		aggregateId: Long,
		eventType: EventType,
		payloadJson: String
	) {
		outboxEventRepository.save(
			OutboxEventEntity(
				aggregateType = aggregateType,
				aggregateId = aggregateId,
				eventType = eventType,
				payload = payloadJson
			)
		)
	}
}