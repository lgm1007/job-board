package org.lgm.jobboard.jobposting.application.port

import org.lgm.jobboard.jobposting.application.query.JobPostingDetailView
import org.lgm.jobboard.jobposting.application.query.JobPostingListItemView
import org.lgm.jobboard.jobposting.application.query.JobPostingSearchCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface JobPostingQueryPort {
	fun findDetailById(jobPostingId: Long): JobPostingDetailView
	fun search(condition: JobPostingSearchCondition, pageable: Pageable): Page<JobPostingListItemView>
}