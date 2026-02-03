package org.lgm.jobboard.search.application.service

import org.lgm.jobboard.jobposting.application.port.JobPostingQueryPort
import org.lgm.jobboard.jobposting.application.query.JobPostingListItemView
import org.lgm.jobboard.jobposting.application.query.PageView
import org.lgm.jobboard.search.application.port.JobPostingSearchPort
import org.lgm.jobboard.search.application.query.JobPostingSearchCondition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

@Service
class JobPostingSearchService(
	private val searchPort: JobPostingSearchPort,
	private val jobPostingQueryPort: JobPostingQueryPort
) {
	@Transactional(readOnly = true)
	fun search(condition: JobPostingSearchCondition, page: Int, size: Int): PageView<JobPostingListItemView> {
		val safePage = page.coerceAtLeast(0)
		val safeSize = size.coerceIn(1, 100)

		val result = searchPort.search(condition, safePage, safeSize)
		val items = if (result.ids.isEmpty()) emptyList()
			else jobPostingQueryPort.findListByIdsPreserveOrder(result.ids)

		val totalPages = if (result.total == 0L) 0
			else ceil(result.total.toDouble() / safeSize).toInt()

		return PageView(
			items = items,
			page = safePage,
			size = safeSize,
			totalItems = result.total,
			totalPages = totalPages
		)
	}
}