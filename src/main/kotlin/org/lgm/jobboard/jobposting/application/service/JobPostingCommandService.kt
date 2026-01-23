package org.lgm.jobboard.jobposting.application.service

import org.lgm.jobboard.jobposting.application.dto.CreateJobPostingResult
import org.lgm.jobboard.jobposting.application.dto.CreatedJobPostingCommand
import org.lgm.jobboard.jobposting.application.port.CompanyQueryPort
import org.lgm.jobboard.jobposting.application.port.JobPostingCommandPort
import org.lgm.jobboard.jobposting.application.port.SkillCommandPort
import org.lgm.jobboard.jobposting.domain.model.ExperienceRange
import org.lgm.jobboard.jobposting.domain.model.JobPosting
import org.lgm.jobboard.jobposting.domain.model.JobPostingCreatedEvent
import org.lgm.jobboard.outbox.application.port.OutboxCommandPort
import org.lgm.jobboard.outbox.domain.type.AggregateType
import org.lgm.jobboard.outbox.domain.type.EventType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JobPostingCommandService(
	private val companyQueryPort: CompanyQueryPort,
	private val skillCommandPort: SkillCommandPort,
	private val jobPostingCommandPort: JobPostingCommandPort,
	private val outboxCommandPort: OutboxCommandPort
) {
	@Transactional
	fun create(command: CreatedJobPostingCommand): CreateJobPostingResult {
		// 회사 없으면 에러
		if (!companyQueryPort.existsById(command.companyId)) {
			throw IllegalArgumentException("Company not found. companyId=${command.companyId}")
		}

		// 스킬 자동 생성/정규화
		val normalizedSkills = skillCommandPort.upsertAll(command.skills)

		// 도메인 생성
		val jobPosting = JobPosting.createNew(
			companyId = command.companyId,
			title = command.title,
			description = command.description,
			employmentType = command.employmentType,
			experience = ExperienceRange(command.experienceMin, command.experienceMax),
			location = command.location,
			skills = normalizedSkills
		)

		// 저장 후 ID 확정
		val saved = jobPostingCommandPort.save(jobPosting)

		// 도메인 이벤트 -> outbox 적재
		saved.pullDomainEvents().forEach { event ->
			when (event) {
				is JobPostingCreatedEvent -> {
					val payload = """
						{
							"jobPostingId": ${event.jobPostingId},
							"companyId": ${event.companyId},
							"event": "JOB_POSTING_CREATED"
						}
					""".trimIndent()

					outboxCommandPort.enqueue(
						aggregateType = AggregateType.JOB_POSTING,
						aggregateId = event.jobPostingId,
						eventType = EventType.UPSERT,
						payloadJson = payload
					)
				}
			}
		}

		return CreateJobPostingResult(jobPostingId = saved.id!!)
	}
}
