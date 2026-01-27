package org.lgm.jobboard.jobposting.application.query

enum class JobPostingSort(val property: String) {
	CREATED_AT("created_at"),
	TITLE("title");

	companion object {
		fun fromParam(value: String?): JobPostingSort =
			when (value?.trim()?.lowercase()) {
				null, "", "createdat", "created_at" -> CREATED_AT
				"title" -> TITLE
				else -> throw IllegalArgumentException("unsupported sort: $value")
			}
	}
}