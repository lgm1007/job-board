package org.lgm.jobboard.search.api

import org.lgm.jobboard.jobposting.api.response.JobPostingListResponse
import org.lgm.jobboard.search.application.query.JobPostingSearchCondition
import org.lgm.jobboard.search.application.service.JobPostingSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search/job-postings")
class JobPostingSearchController(
	private val jobPostingSearchService: JobPostingSearchService
) {
	@GetMapping
	fun search(
		@RequestParam(required = false) q: String?,
		@RequestParam(required = false) companyId: Long?,
		@RequestParam(required = false) skill: String?,
		@RequestParam(required = false) status: String?,
		@RequestParam(defaultValue = "0") page: Int,
		@RequestParam(defaultValue = "20") size: Int
	): JobPostingListResponse {
		val condition = JobPostingSearchCondition(
			q = q,
			companyId = companyId,
			skill = skill,
			status = status
		)

		val result = jobPostingSearchService.search(condition, page, size)
		return JobPostingListResponse.from(result)
	}
}