package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.jobposting.application.port.SkillCommandPort
import org.lgm.jobboard.jobposting.infra.persistence.SkillEntity
import org.lgm.jobboard.jobposting.infra.persistence.SkillRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SkillCommandAdapter(
	private val skillRepository: SkillRepository
) : SkillCommandPort {
	@Transactional
	override fun upsertAll(skills: Set<String>): Set<String> {
		val normalized = skills.map { it.trim() }
			.filter { it.isNotBlank() }
			.toSet()

		if (normalized.isEmpty()) return emptySet()

		val existing = skillRepository.findAllByNameIn(normalized).map {
			it.name
		}.toSet()
		// 새로 생성되는 기술 스택 = 입력받은 목록 - 존재하는 목록
		val toCreate = normalized - existing

		val skillEntities = toCreate.map {
			SkillEntity(name = it)
		}

		skillRepository.saveAll(skillEntities)

		// 최종적으로 존재하는 기술 스택 컬렉션 반환
		return skillRepository.findAllByNameIn(normalized)
			.map { it.name }
			.toSet()
	}
}