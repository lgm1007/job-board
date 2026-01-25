package org.lgm.jobboard.jobposting.api.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.lgm.jobboard.jobposting.domain.type.EmploymentType

/**
 * DTO는 nullable로 받고, 서비스 호출 전에 unwrap
 */
data class CreateJobPostingRequest(
	@field:NotNull
	val companyId: Long?,

	@field:NotBlank
	@field:Size(max = 300)
	val title: String?,

	@field:NotBlank
	val description: String?,

	@field:NotBlank
	val employmentType: EmploymentType?,

	@field:NotNull
	@field:Min(0)
	val experienceMin: Int?,

	@field:Min(0)
	val experienceMax: Int?,

	@field:NotBlank
	@field:Size(max = 200)
	val location: String?,

	val skills: Set<@NotBlank String>? = emptySet()
)
