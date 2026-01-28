package org.lgm.jobboard.outbox.application.handler

import org.lgm.jobboard.outbox.application.query.OutboxEventView

interface OutboxEventHandler {
	fun canHandle(event: OutboxEventView): Boolean

	fun handle(event: OutboxEventView)
}