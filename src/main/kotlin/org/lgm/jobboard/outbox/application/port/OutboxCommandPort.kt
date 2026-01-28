package org.lgm.jobboard.outbox.application.port

import org.lgm.jobboard.outbox.domain.type.AggregateType
import org.lgm.jobboard.outbox.domain.type.EventType
import java.time.OffsetDateTime

interface OutboxCommandPort {
	fun enqueue(
		aggregateType: AggregateType,
		aggregateId: Long,
		eventType: EventType,
		payloadJson: String
	)

	fun markProcessing(id: Long)

	fun markDone(id: Long)

	fun markFailed(id: Long, errorMessage: String, nextAvailableAt: OffsetDateTime)

	fun requeue(id: Long, nextAvailableAt: OffsetDateTime)
}