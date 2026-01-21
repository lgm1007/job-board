package org.lgm.jobboard.jobposting.infra.persistence.pk

import jakarta.persistence.Column
import java.io.Serializable

data class JobPostingSkillId(
	@Column(name = "job_posting_id")
	val jobPostingId: Long = 0L,

	@Column(name = "skill_id")
	val skillId: Long = 0L
) : Serializable