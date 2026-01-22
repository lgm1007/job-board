package org.lgm.jobboard.jobposting.application.port

import org.lgm.jobboard.jobposting.domain.model.JobPosting

interface JobPostingCommandPort {
	fun save(jobPosting: JobPosting): JobPosting
}