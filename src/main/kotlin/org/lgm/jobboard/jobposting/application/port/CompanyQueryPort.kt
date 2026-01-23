package org.lgm.jobboard.jobposting.application.port

interface CompanyQueryPort {
	fun existsById(companyId: Long): Boolean
}