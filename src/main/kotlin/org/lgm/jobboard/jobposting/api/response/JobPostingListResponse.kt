package org.lgm.jobboard.jobposting.api.response

import org.lgm.jobboard.jobposting.application.query.JobPostingListItemView
import org.lgm.jobboard.jobposting.application.query.PageView
import java.time.OffsetDateTime

data class JobPostingListResponse(
	val items: List<Item>,
	val page: Int,
	val size: Int,
	val totalItems: Long,
	val totalPages: Int
) {
	data class Item(
		val id: Long,
		val title: String,
		val location: String,
		val status: String,
		val createdAt: OffsetDateTime?,
		val company: Company,
		val skills: Set<String>
	) {
		data class Company(
			val id: Long,
			val name: String
		)
	}

	companion object {
		fun from(page: PageView<JobPostingListItemView>): JobPostingListResponse =
			JobPostingListResponse(
				items = page.items.map { view ->
					Item(
						id = view.id,
						title = view.title,
						location = view.location,
						status = view.status,
						createdAt = view.createdAt,
						company = Item.Company(
							id = view.company.id,
							name = view.company.name
						),
						skills = view.skills
					)
				},
				page = page.page,
				size = page.size,
				totalItems = page.totalItems,
				totalPages = page.totalPages
			)
	}
}
