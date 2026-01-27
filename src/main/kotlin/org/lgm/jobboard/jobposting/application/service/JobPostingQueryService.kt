package org.lgm.jobboard.jobposting.application.service

import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.lgm.jobboard.jobposting.application.query.JobPostingDetailView
import org.lgm.jobboard.jobposting.application.query.JobPostingListItemView
import org.lgm.jobboard.jobposting.application.query.JobPostingSearchCondition
import org.lgm.jobboard.jobposting.application.query.PageView
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

	@Transactional(readOnly = true)
	fun search(condition: JobPostingSearchCondition, page: Int, size: Int): PageView<JobPostingListItemView> {
		val safeSize = size.coerceIn(1, 100)
		val pageable = PageRequest.of(page.coerceAtLeast(0), safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))

		val resultPage = jobPostingQueryPort.search(condition, pageable)
		return PageView(
			items = resultPage.content,
			page = resultPage.number,
			size = resultPage.size,
			totalItems = resultPage.totalElements,
			totalPages = resultPage.totalPages
		)
	}
}