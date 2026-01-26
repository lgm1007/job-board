package org.lgm.jobboard.jobposting.application.query

data class PageView<T>(
	val items: List<T>,
	val page: Int,
	val size: Int,
	val totalItems: Long,
	val totalPages: Int
)
