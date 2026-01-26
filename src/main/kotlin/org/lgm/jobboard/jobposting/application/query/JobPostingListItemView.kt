package org.lgm.jobboard.jobposting.application.query

import java.time.OffsetDateTime

data class JobPostingListItemView(
	val id: Long,
	val title: String,
	val location: String,
	val status: String,
	val createdAt: OffsetDateTime?,
	val company: CompanySummaryView,
	val skills: Set<String>
)
