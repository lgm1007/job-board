package org.lgm.jobboard.jobposting.application.service

import org.lgm.jobboard.jobposting.application.dto.JobPostingDetailView
import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JobPostingQueryService(
	private val jobPostingQueryPort: JobPostingQueryPort
) {
	@Transactional(readOnly = true)
	fun getDetail(jobPostingId: Long): JobPostingDetailView {
		return jobPostingQueryPort.findDetailById(jobPostingId)
	}
}