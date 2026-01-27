package org.lgm.jobboard.jobposting.application.service

import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.lgm.jobboard.jobposting.application.query.*
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
	fun search(
		condition: JobPostingSearchCondition,
		page: Int,
		size: Int,
		sort: JobPostingSort,
		dir: SortDirection
	): PageView<JobPostingListItemView> {
		val safeSize = size.coerceIn(1, 100)
		val safePage = page.coerceAtLeast(0)

		val direction = if (dir == SortDirection.ASC) Sort.Direction.ASC else Sort.Direction.DESC
		val pageable = PageRequest.of(
			safePage,
			safeSize,
			Sort.by(direction, sort.property)
		)

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