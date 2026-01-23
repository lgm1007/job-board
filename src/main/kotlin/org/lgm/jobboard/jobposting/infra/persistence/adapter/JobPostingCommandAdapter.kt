package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.company.infra.persistence.CompanyRepository
import org.lgm.jobboard.jobposting.application.port.JobPostingCommandPort
import org.lgm.jobboard.jobposting.domain.model.ExperienceRange
import org.lgm.jobboard.jobposting.domain.model.JobPosting
import org.lgm.jobboard.jobposting.infra.persistence.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JobPostingCommandAdapter(
	private val companyRepository: CompanyRepository,
	private val jobPostingRepository: JobPostingRepository,
	private val skillRepository: SkillRepository,
	private val jobPostingSkillRepository: JobPostingSkillRepository
) : JobPostingCommandPort {
	@Transactional
	override fun save(jobPosting: JobPosting): JobPosting {
		val company = companyRepository.findById(jobPosting.companyId)
			.orElseThrow { IllegalArgumentException("Company not found. companyId=${jobPosting.companyId}") }

		// 도메인 -> 엔티티
		val savedEntity = jobPostingRepository.save(
			JobPostingEntity(
				company = company,
				title = jobPosting.title,
				description = jobPosting.description,
				employmentType = jobPosting.employmentType,
				experienceMin = jobPosting.experience.min,
				experienceMax = jobPosting.experience.max,
				location = jobPosting.location,
				status = jobPosting.status
			)
		)

		// 스킬 매핑 저장
		val skills = jobPosting.skills
		if (skills.isNotEmpty()) {
			val skills = skillRepository.findAllByNameIn(skills)
			skills.forEach { skill ->
				jobPostingSkillRepository.save(
					JobPostingSkillEntity(
						jobPosting = savedEntity,
						skill = skill,
					)
				)
			}
		}

		// 엔티티 -> 도메인(저장 후 ID 주입 및 생성 이벤트 발생)
		val normalizedSkills = skills.map { it.trim() }
			.filter { it.isNotBlank() }
			.toSet()

		val jobPostingDomain = JobPosting.createNew(
			companyId = savedEntity.company.id!!,
			title = savedEntity.title,
			description = savedEntity.description,
			employmentType = savedEntity.employmentType,
			experience = ExperienceRange(savedEntity.experienceMin, savedEntity.experienceMax),
			location = savedEntity.location,
			skills = normalizedSkills
		)

		return jobPostingDomain.withPersistedId(savedEntity.id!!)
	}
}
