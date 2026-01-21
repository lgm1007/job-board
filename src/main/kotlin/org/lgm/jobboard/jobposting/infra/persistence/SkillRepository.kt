package org.lgm.jobboard.jobposting.infra.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface SkillRepository : JpaRepository<SkillEntity, Long> {
	fun findByName(name: String): SkillEntity?
	fun findAllByNameIn(names: List<String>): List<SkillEntity>
}