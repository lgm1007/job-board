package org.lgm.jobboard.search.application.query

data class JobPostingSearchCondition(
	val q: String?, // 키워드
	val companyId: Long?,
	val skill: String?,
	val status: String?
)
