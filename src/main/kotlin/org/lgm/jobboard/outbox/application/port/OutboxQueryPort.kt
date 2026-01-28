package org.lgm.jobboard.outbox.application.port

import org.lgm.jobboard.outbox.application.query.OutboxEventView
import java.time.OffsetDateTime

interface OutboxQueryPort {
	fun findDueEventIds(now: OffsetDateTime, limit: Int): List<Long>

	fun lockById(id: Long): OutboxEventView?    // 비관적 락으로 한 건씩 가져오기
}