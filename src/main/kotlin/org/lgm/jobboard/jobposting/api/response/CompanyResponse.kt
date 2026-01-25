package org.lgm.jobboard.jobposting.api.response

data class CompanyResponse(
	val id: Long,
	val name: String,
	val website: String?,
	val industry: String?
)
