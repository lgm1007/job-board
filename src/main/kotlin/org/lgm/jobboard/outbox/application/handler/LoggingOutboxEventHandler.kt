package org.lgm.jobboard.outbox.application.handler

import org.lgm.jobboard.outbox.application.query.OutboxEventView
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingOutboxEventHandler : OutboxEventHandler {
	private val log = LoggerFactory.getLogger(javaClass)

	override fun canHandle(event: OutboxEventView): Boolean {
		// 전부 처리한다고 가정
		return true
	}

	override fun handle(event: OutboxEventView) {
		// TODO: 추후 OpenSearch 색인 호출이 들어갈 것
		log.info(
			"Handled outbox event. id={}, aggregateType={}, aggregateId={}, eventType={}, payload={}",
			event.id, event.aggregateType, event.aggregateId, event.eventType, event.payloadJson
		)
	}
}