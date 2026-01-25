package org.lgm.jobboard.jobposting.api

import jakarta.validation.Valid
import org.lgm.jobboard.jobposting.api.request.CreateJobPostingRequest
import org.lgm.jobboard.jobposting.application.dto.CreateJobPostingCommand
import org.lgm.jobboard.jobposting.application.service.JobPostingCommandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/job-postings")
class JobPostingController(
	private val jobPostingCommandService: JobPostingCommandService
) {
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