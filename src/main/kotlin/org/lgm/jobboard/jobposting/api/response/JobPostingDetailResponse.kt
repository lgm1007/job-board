package org.lgm.jobboard.jobposting.api.response

import org.lgm.jobboard.jobposting.application.query.JobPostingDetailView
import java.time.OffsetDateTime

data class JobPostingDetailResponse(
	val id: Long,
	val title: String,
	val description: String,
	val employmentType: String,
	val experienceMin: Int,
	val experienceMax: Int?,
	val location: String,
	val status: String,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
	val company: CompanyResponse,
	val skills: Set<String>
) {
	data class CompanyResponse(
		val id: Long,
		val name: String,
		val website: String?,
		val industry: String?
	)

	companion object {
		fun from(view: JobPostingDetailView): JobPostingDetailResponse =
			JobPostingDetailResponse(
				id = view.jobPostingId,
				title = view.title,
				description = view.description,
				employmentType = view.employmentType,
				experienceMin = view.experienceMin,
				experienceMax = view.experienceMax,
				location = view.location,
				status = view.status,
				createdAt = view.createdAt,
				updatedAt = view.updatedAt,
				company = CompanyResponse(
					id = view.company.companyId,
					name = view.company.name,
					website = view.company.website,
					industry = view.company.industry
				),
				skills = view.skills
			)
	}
}
