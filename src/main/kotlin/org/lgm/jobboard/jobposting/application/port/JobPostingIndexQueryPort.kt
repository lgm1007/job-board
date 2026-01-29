package org.lgm.jobboard.jobposting.application.port

import org.lgm.jobboard.jobposting.application.query.JobPostingIndexDocument

interface JobPostingIndexQueryPort {
	fun getIndexDocument(jobPostingId: Long): JobPostingIndexDocument?
}