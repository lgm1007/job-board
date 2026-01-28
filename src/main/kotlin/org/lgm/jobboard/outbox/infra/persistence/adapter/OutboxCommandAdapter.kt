package org.lgm.jobboard.outbox.infra.persistence.adapter

import org.lgm.jobboard.outbox.application.port.OutboxCommandPort
import org.lgm.jobboard.outbox.domain.type.AggregateType
import org.lgm.jobboard.outbox.domain.type.EventType
import org.lgm.jobboard.outbox.infra.persistence.OutboxEventEntity
import org.lgm.jobboard.outbox.infra.persistence.OutboxEventRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

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

	@Transactional
	override fun markProcessing(id: Long) {
		val event = outboxEventRepository.findById(id).orElseThrow()
		event.markProcessing()
	}

	@Transactional
	override fun markDone(id: Long) {
		val event = outboxEventRepository.findById(id).orElseThrow()
		event.markDone()
	}

	@Transactional
	override fun markFailed(id: Long, errorMessage: String, nextAvailableAt: OffsetDateTime) {
		val event = outboxEventRepository.findById(id).orElseThrow()
		event.markFailed(errorMessage, nextAvailableAt)
	}

	@Transactional
	override fun requeue(id: Long, nextAvailableAt: OffsetDateTime) {
		val event = outboxEventRepository.findById(id).orElseThrow()
		event.requeue(nextAvailableAt)
	}
}