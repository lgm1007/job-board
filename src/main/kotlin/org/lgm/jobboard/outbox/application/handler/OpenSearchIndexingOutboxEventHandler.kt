package org.lgm.jobboard.outbox.application.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.lgm.jobboard.jobposting.application.port.JobPostingIndexCommandPort
import org.lgm.jobboard.jobposting.application.port.JobPostingIndexQueryPort
import org.lgm.jobboard.outbox.application.query.OutboxEventView
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OpenSearchIndexingOutboxEventHandler(
	private val objectMapper: ObjectMapper,
	private val jobPostingIndexQueryPort: JobPostingIndexQueryPort,
	private val jobPostingIndexCommandPort: JobPostingIndexCommandPort
) : OutboxEventHandler {
	private val log = LoggerFactory.getLogger(javaClass)

	override fun canHandle(event: OutboxEventView): Boolean {
		// JOB_POSTING만 처리한다고 가정
		return event.aggregateType == "JOB_POSTING"
	}

	override fun handle(event: OutboxEventView) {
		val root = objectMapper.readTree(event.payloadJson)
		val jobPostingId = root.path("jobPostingId").asLong(0)
		if (jobPostingId <= 0) {
			throw IllegalArgumentException("Invalid payload: jobPostingId missing. outboxId=${event.id}")
		}

		when (event.eventType) {
			"UPSER" -> {
				val document = jobPostingIndexQueryPort.getIndexDocument(jobPostingId)
					?: run {
						// DB에서 이미 삭제된 경우: 인덱스도 삭제 (정책 사항)
						log.info("JobPosting not found in DB, deleting from index. id={}", jobPostingId)
						jobPostingIndexCommandPort.delete(jobPostingId)
						return
					}

				// OpenSearch upsert: 문서가 있으면 update, 없으면 insert
				jobPostingIndexCommandPort.upsert(document)
			}

			"DELETE" -> {
				jobPostingIndexCommandPort.delete(jobPostingId)
			}

			else -> {
				// 예상치 못한 이벤트는 실패 처리하여 관측/재시도 가능하도록 함
				throw IllegalStateException("Unsupported event type=${event.eventType}")
			}
		}
	}
}