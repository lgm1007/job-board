package org.lgm.jobboard.outbox.application.port

import org.lgm.jobboard.outbox.domain.AggregateType
import org.lgm.jobboard.outbox.domain.EventType

interface OutboxCommandPort {
	fun enqueue(
		aggregateType: AggregateType,
		aggregateId: Long,
		eventType: EventType,
		payloadJson: String
	)
}