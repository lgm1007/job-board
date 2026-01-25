package org.lgm.jobboard.jobposting.application.dto

import java.time.OffsetDateTime

data class JobPostingDetailView(
	val jobPostingId: Long,
	val title: String,
	val description: String,
	val employmentType: String,
	val experienceMin: Int,
	val experienceMax: Int?,
	val location: String,
	val status: String,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
	val company: CompanyView,
	val skills: Set<String>
)
