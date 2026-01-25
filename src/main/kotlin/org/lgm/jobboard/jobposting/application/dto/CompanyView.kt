package org.lgm.jobboard.jobposting.application.dto

data class CompanyView(
	val companyId: Long,
	val name: String,
	val website: String?,
	val industry: String?
)
