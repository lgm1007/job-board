package org.lgm.jobboard.company.infra.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<CompanyEntity, Long> {
	fun findByName(name: String): CompanyEntity?
	fun existsByName(name: String): Boolean
}