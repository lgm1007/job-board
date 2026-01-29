package org.lgm.jobboard.jobposting.application.port

import org.lgm.jobboard.jobposting.application.query.JobPostingIndexDocument

interface JobPostingIndexCommandPort {
	fun upsert(document: JobPostingIndexDocument)

	fun delete(jobPostingId: Long)
}