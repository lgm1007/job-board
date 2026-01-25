package org.lgm.jobboard.jobposting.application.port

import org.lgm.jobboard.jobposting.application.dto.JobPostingDetailView

interface JobPostingQueryPort {
	fun findDetailById(jobPostingId: Long): JobPostingDetailView
}