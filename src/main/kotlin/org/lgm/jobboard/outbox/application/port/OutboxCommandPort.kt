package org.lgm.jobboard.outbox.application.port

import org.lgm.jobboard.outbox.domain.type.AggregateType
import org.lgm.jobboard.outbox.domain.type.EventType

interface OutboxCommandPort {
	fun enqueue(
		aggregateType: AggregateType,
		aggregateId: Long,
		eventType: EventType,
		payloadJson: String
	)
}