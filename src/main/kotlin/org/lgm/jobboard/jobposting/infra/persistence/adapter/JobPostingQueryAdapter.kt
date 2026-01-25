package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.jobposting.application.dto.CompanyView
import org.lgm.jobboard.jobposting.application.dto.JobPostingDetailView
import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingRepository
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingSkillRepository
import org.springframework.stereotype.Component

@Component
class JobPostingQueryAdapter(
	private val jobPostingRepository: JobPostingRepository,
	private val jobPostingSkillRepository: JobPostingSkillRepository
) : JobPostingQueryPort {
	override fun findDetailById(jobPostingId: Long): JobPostingDetailView {
		val jobPosting = jobPostingRepository.findByIdWithCompany(jobPostingId)
			?: throw IllegalArgumentException("JobPosting not found. id = $jobPostingId")
		val skills = jobPostingSkillRepository.findAllByJobPostingIdWithSkill(jobPostingId)
			.map { it.skill.name }
			.toSet()
		val company = jobPosting.company

		return JobPostingDetailView(
			jobPostingId = jobPosting.id!!,
			title = jobPosting.title,
			description = jobPosting.description,
			employmentType = jobPosting.employmentType.description,
			experienceMin = jobPosting.experienceMin,
			experienceMax = jobPosting.experienceMax,
			location = jobPosting.location,
			status = jobPosting.status.description,
			createdAt = jobPosting.createdAt,
			updatedAt = jobPosting.updatedAt,
			company = CompanyView(
				companyId = company.id!!,
				name = company.name,
				website = company.website,
				industry = company.industry
			),
			skills = skills
		)
	}
}