package org.lgm.jobboard.jobposting.application.query

enum class SortDirection {
	ASC,
	DESC;

	companion object {
		fun fromParam(value: String?): SortDirection =
			when (value?.trim()?.lowercase()) {
				"asc" -> ASC
				null, "", "desc" -> DESC
				else -> throw IllegalArgumentException("unsupported sort: $value")
			}
	}
}