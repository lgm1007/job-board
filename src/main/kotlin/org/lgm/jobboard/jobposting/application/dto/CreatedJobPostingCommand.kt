package org.lgm.jobboard.jobposting.application.dto

import org.lgm.jobboard.jobposting.domain.type.EmploymentType

data class CreatedJobPostingCommand(
	val companyId: Long,
	val title: String,
	val description: String,
	val employmentType: EmploymentType,
	val experienceMin: Int,
	val experienceMax: Int?,
	val location: String,
	val skills: Set<String>
)