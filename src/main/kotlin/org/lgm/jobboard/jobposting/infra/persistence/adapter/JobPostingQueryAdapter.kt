package org.lgm.jobboard.jobposting.infra.persistence.adapter

import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.lgm.jobboard.jobposting.application.query.*
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingEntity
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingRepository
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingSkillRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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

	override fun search(
		condition: JobPostingSearchCondition,
		pageable: Pageable
	): Page<JobPostingListItemView> {
		val skill = condition.skill?.trim()?.takeIf { it.isNotBlank() }

		val idPage: Page<Long> =
			if (skill == null) {
				jobPostingRepository.searchIds(
					q = condition.q,
					companyId = condition.companyId,
					status = condition.status,
					pageable = pageable
				)
			} else {
				jobPostingSkillRepository.searchIdsBySkill(
					companyId = condition.companyId,
					status = condition.status,
					skill = skill,
					pageable = PageRequest.of(
						pageable.pageNumber,
						pageable.pageSize
					)
				)
			}

		val ids = idPage.content
		if (ids.isEmpty()) {
			return PageImpl(emptyList(), pageable, idPage.totalElements)
		}

		// company fetch join
		val postings = jobPostingRepository.findAllByIdInWithCompany(ids)

		// id 순서대로 정렬
		val postingById: Map<Long, JobPostingEntity> = postings.associateBy { it.id!! }
		val orderedPostings = ids.mapNotNull { postingById[it] }

		// skills 한번에 조회 후 그룹핑
		val skillRows = jobPostingSkillRepository.findAllByJobPostingIdsWithSkill(ids)
		val skillsByPostingId: Map<Long, Set<String>> =
			skillRows.groupBy { it.jobPosting.id!! }
				.mapValues { (_, rows) -> rows.map { it.skill.name }.toSet() }

		val items = orderedPostings.map { jp ->
			val company = jp.company
			JobPostingListItemView(
				id = jp.id!!,
				title = jp.title,
				location = jp.location,
				status = jp.status.description,
				createdAt = jp.createdAt,
				company = CompanySummaryView(
					id = company.id!!,
					name = company.name
				),
				skills = skillsByPostingId[jp.id!!] ?: emptySet()
			)
		}

		return PageImpl(items, pageable, idPage.totalElements)
	}

	override fun findListByIdsPreserveOrder(ids: List<Long>): List<JobPostingListItemView> {
		// company fetch join
		val postings = jobPostingRepository.findAllByIdInWithCompany(ids)

		// id 순서대로 정렬
		val postingById: Map<Long, JobPostingEntity> = postings.associateBy { it.id!! }
		val orderedPostings = ids.mapNotNull { postingById[it] }

		// skills 한번에 조회 후 그룹핑
		val skillRows = jobPostingSkillRepository.findAllByJobPostingIdsWithSkill(ids)
		val skillsByPostingId: Map<Long, Set<String>> =
			skillRows.groupBy { it.jobPosting.id!! }
				.mapValues { (_, rows) -> rows.map { it.skill.name }.toSet() }

		return orderedPostings.map { jp ->
			val company = jp.company
			JobPostingListItemView(
				id = jp.id!!,
				title = jp.title,
				location = jp.location,
				status = jp.status.description,
				createdAt = jp.createdAt,
				company = CompanySummaryView(
					id = company.id!!,
					name = company.name
				),
				skills = skillsByPostingId[jp.id!!] ?: emptySet()
			)
		}
	}

	override fun findAllIds(): List<Long> {
		return jobPostingRepository.findAllIds()
	}
}