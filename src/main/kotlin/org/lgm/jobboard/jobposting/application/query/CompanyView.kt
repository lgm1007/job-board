package org.lgm.jobboard.jobposting.application.query

data class CompanyView(
	val companyId: Long,
	val name: String,
	val website: String?,
	val industry: String?
)
