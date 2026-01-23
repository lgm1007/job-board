package org.lgm.jobboard.jobposting.domain.model

import java.time.OffsetDateTime

/**
 * JobPosting 생성 이벤트
 */
data class JobPostingCreatedEvent(
	val jobPostingId: Long,
	val companyId: Long,
	override val occurredAt: OffsetDateTime = OffsetDateTime.now()
) : DomainEvent
