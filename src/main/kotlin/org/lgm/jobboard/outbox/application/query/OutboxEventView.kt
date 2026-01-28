package org.lgm.jobboard.outbox.application.query

import java.time.OffsetDateTime

data class OutboxEventView(
	val id: Long,
	val aggregateType: String,
	val aggregateId: Long,
	val eventType: String,
	val payloadJson: String,
	val status: String,
	val attempts: Int,
	val availableAt: OffsetDateTime
)
