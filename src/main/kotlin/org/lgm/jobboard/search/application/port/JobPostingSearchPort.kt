package org.lgm.jobboard.search.application.port

import org.lgm.jobboard.search.application.query.JobPostingSearchCondition
import org.lgm.jobboard.search.application.query.SearchResult

interface JobPostingSearchPort {
	fun search(condition: JobPostingSearchCondition, page: Int, size: Int): SearchResult
}
