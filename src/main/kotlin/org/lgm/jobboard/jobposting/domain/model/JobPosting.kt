package org.lgm.jobboard.jobposting.domain.model

import org.lgm.jobboard.jobposting.domain.EmploymentType
import org.lgm.jobboard.jobposting.domain.JobPostingStatus

class JobPosting private constructor(
	val id: Long?,
	val companyId: Long,
	title: String,
	description: String,
	employmentType: EmploymentType,
	experience: ExperienceRange,
	location: String,
	status: JobPostingStatus,
	skills: Set<String>
){
	private val domainEvents = mutableListOf<DomainEvent>()

	var title: String = title
		private set
	var description: String = description
		private set
	var employmentType: EmploymentType = employmentType
		private set
	var experience: ExperienceRange = experience
		private set
	var location: String = location
		private set
	var status: JobPostingStatus = status
		private set
	var skills: Set<String> = skills
		private set

	init {
		require(title.isNotBlank()) { "title cannot be blank" }
		require(description.isNotBlank()) { "description cannot be blank" }
		require(location.isNotBlank()) { "location cannot be blank" }
	}

	/**
	 * 채용공고 마감
	 */
	fun close() {
		if (status == JobPostingStatus.CLOSE) return
		status = JobPostingStatus.CLOSE
	}

	/**
	 * 이벤트 꺼내오기
	 */
	fun pullDomainEvents(): List<DomainEvent> = domainEvents.toList().also { domainEvents.clear() } // 가져온 후 리스트 비우기

	companion object {
		fun createNew(
			companyId: Long,
			title: String,
			description: String,
			employmentType: EmploymentType,
			experience: ExperienceRange,
			location: String,
			skills: Set<String>
		): JobPosting {
			val normalizedSkills = skills.map { it.trim() }
				.filter { it.isNotBlank() }
				.toSet()

			val jobPosting = JobPosting(
				id = null,
				companyId = companyId,
				title = title.trim(),
				description = description.trim(),
				employmentType = employmentType,
				experience = experience,
				location = location.trim(),
				status = JobPostingStatus.OPEN,
				skills = normalizedSkills
			)

			// ID가 없는 Aggregate 생성
			return jobPosting
		}
	}

	/**
	 * 저장 후 영속 ID 주입
	 */
	fun withPersistedId(persistedId: Long): JobPosting {
		val persisted = JobPosting(
			id = persistedId,
			companyId = companyId,
			title = title,
			description = description,
			employmentType = employmentType,
			experience = experience,
			location = location,
			status = status,
			skills = skills
		)
		// 이벤트 기록
		persisted.domainEvents.add(
			JobPostingCreatedEvent(
				jobPostingId = persistedId,
				companyId = companyId,
			)
		)
		return persisted
	}
}