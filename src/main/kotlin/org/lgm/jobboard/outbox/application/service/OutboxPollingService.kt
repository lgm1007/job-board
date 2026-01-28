package org.lgm.jobboard.outbox.application.service

import org.lgm.jobboard.outbox.application.handler.OutboxEventHandler
import org.lgm.jobboard.outbox.application.port.OutboxCommandPort
import org.lgm.jobboard.outbox.application.port.OutboxQueryPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.OffsetDateTime
import kotlin.math.min

@Service
class OutboxPollingService(
	private val outboxQueryPort: OutboxQueryPort,
	private val outboxCommandPort: OutboxCommandPort,
	private val handlers: List<OutboxEventHandler>,
	transactionManager: PlatformTransactionManager
) {
	private val log = LoggerFactory.getLogger(javaClass)
	private val tx = TransactionTemplate(transactionManager)

	fun pollOnce(batchSize: Int = 50) {
		val now = OffsetDateTime.now()
		val ids = outboxQueryPort.findDueEventIds(now, batchSize)
		if (ids.isEmpty()) return

		ids.forEach { id ->
			// 이벤트 1건당 트랜잭션 분리
			tx.executeWithoutResult {
				processOne(id)
			}
		}
	}

	fun processOne(id: Long) {
		val locked = outboxQueryPort.lockById(id) ?: return

		// 이미 누가 처리 중이거나 상태가 변할 수 있음 (경쟁 상태 방어)
		// 여기서 DB row Lock이 잡혀 있지만 체크
		outboxCommandPort.markProcessing(id)

		try {
			val handler = handlers.firstOrNull { it.canHandle(locked) }
				?: throw IllegalStateException("No handler found for outbox event id=$id")

			handler.handle(event = locked)
		} catch (e: Exception) {
			val attemps = locked.attempts + 1
			val next = computeBackoff(OffsetDateTime.now(), attemps)
			val msg = e.message ?: e.javaClass.simpleName
			log.warn("Outbox handling failed. id={}, attemps={}, nextAvailableAt={}, error={}",
				id, attemps, next, msg)
			outboxCommandPort.markFailed(id, msg, next)
		}
	}

	/**
	 * 단순 backoff: 2^attemps 초 (최대 5분 캡)
	 * 예: 1회=2s, 2회=4s, 3회=8s ... 최대 300s
	 */
	private fun computeBackoff(now: OffsetDateTime, attempts: Int): OffsetDateTime {
		val seconds = min(300, 1 shl min(attempts, 8)) // 2^attempts, overflow 방지
		return now.plusSeconds(seconds.toLong())
	}
}