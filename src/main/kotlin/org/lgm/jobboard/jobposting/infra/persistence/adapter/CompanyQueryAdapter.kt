package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.company.infra.persistence.CompanyRepository
import org.lgm.jobboard.jobposting.application.port.CompanyQueryPort
import org.springframework.stereotype.Component

@Component
class CompanyQueryAdapter(
	private val companyRepository: CompanyRepository
) : CompanyQueryPort {
	override fun existsById(companyId: Long): Boolean =
		companyRepository.existsById(companyId)

}