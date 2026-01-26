package org.lgm.jobboard.jobposting.application.query

import org.lgm.jobboard.jobposting.domain.type.JobPostingStatus

data class JobPostingSearchCondition(
	val companyId: Long? = null,
	val skill: String? = null,
	val status: JobPostingStatus? = null
)