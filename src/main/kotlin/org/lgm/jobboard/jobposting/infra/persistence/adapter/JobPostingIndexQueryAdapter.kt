package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.jobposting.application.port.JobPostingIndexQueryPort
import org.lgm.jobboard.jobposting.application.query.JobPostingIndexDocument
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingRepository
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingSkillRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JobPostingIndexQueryAdapter(
	private val jobPostingRepository: JobPostingRepository,
	private val jobPostingSkillRepository: JobPostingSkillRepository
) : JobPostingIndexQueryPort {

	@Transactional(readOnly = true)
	override fun getIndexDocument(jobPostingId: Long): JobPostingIndexDocument? {
		val jobPosting = jobPostingRepository.findByIdWithCompany(jobPostingId) ?: return null
		val company = jobPosting.company
		val skills = jobPostingSkillRepository.findAllByJobPostingIdWithSkill(jobPostingId)
			.map { it.skill.name }
			.distinct()
			.sorted()


		return JobPostingIndexDocument(
			id = jobPosting.id!!,
			title = jobPosting.title,
			description = jobPosting.description,
			location = jobPosting.location,
			status = jobPosting.status.name,
			employmentType = jobPosting.status.name,
			experienceMin = jobPosting.experienceMin,
			experienceMax = jobPosting.experienceMax,
			companyId = company.id!!,
			companyName = company.name,
			companyIndustry = company.industry,
			skillNames = skills,
			updatedAt = jobPosting.updatedAt
		)
	}
}