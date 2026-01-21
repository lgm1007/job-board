package org.lgm.jobboard.jobposting.infra.persistence

import jakarta.persistence.*
import org.lgm.jobboard.jobposting.infra.persistence.pk.JobPostingSkillId

@Entity
@Table(name = "job_posting_skill")
class JobPostingSkillEntity(
	@EmbeddedId
	var id: JobPostingSkillId = JobPostingSkillId(),

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("jobPostingId")
	@JoinColumn(name = "job_posting_id", nullable = false)
	var jobPosting: JobPostingEntity,

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("skillId")
	@JoinColumn(name = "skill_id", nullable = false)
	var skill: SkillEntity
)