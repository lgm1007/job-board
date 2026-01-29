package org.lgm.jobboard.jobposting.application.query

import java.time.OffsetDateTime

data class JobPostingIndexDocument(
	val id: Long,
	val title: String,
	val description: String,
	val location: String,
	val status: String,
	val employmentType: String,
	val experienceMin: Int,
	val experienceMax: Int?,
	val companyId: Long,
	val companyName: String,
	val companyIndustry: String?,
	val skillNames: List<String>,
	val updatedAt: OffsetDateTime?
)
