package org.lgm.jobboard.jobposting.api

import jakarta.validation.Valid
import org.lgm.jobboard.jobposting.api.request.CreateJobPostingRequest
import org.lgm.jobboard.jobposting.api.response.JobPostingDetailResponse
import org.lgm.jobboard.jobposting.application.dto.CreateJobPostingCommand
import org.lgm.jobboard.jobposting.application.service.JobPostingCommandService
import org.lgm.jobboard.jobposting.application.service.JobPostingQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/job-postings")
class JobPostingController(
	private val jobPostingQueryService: JobPostingQueryService,
	private val jobPostingCommandService: JobPostingCommandService
) {
	@GetMapping("/{id}")
	fun getDetail(@PathVariable id: Long): JobPostingDetailResponse {
		val jobPostingDetailView = jobPostingQueryService.getDetail(jobPostingId = id)
		return JobPostingDetailResponse.from(view = jobPostingDetailView)
	}

	@PostMapping
	fun create(@Valid @RequestBody request: CreateJobPostingRequest): ResponseEntity<Map<String, Any>> {
		val result = jobPostingCommandService.create(
			CreateJobPostingCommand(
				companyId = request.companyId!!,
				title = request.title!!,
				description = request.description!!,
				employmentType = request.employmentType!!,
				experienceMin = request.experienceMin!!,
				experienceMax = request.experienceMax,
				location = request.location!!,
				skills = request.skills ?: emptySet()
			)
		)

		return ResponseEntity.ok(
			mapOf(
				"jobPostingId" to result.jobPostingId
			)
		)
	}
}