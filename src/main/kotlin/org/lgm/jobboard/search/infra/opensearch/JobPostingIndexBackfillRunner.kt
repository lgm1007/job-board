package org.lgm.jobboard.search.infra.opensearch

import org.lgm.jobboard.jobposting.application.port.JobPostingIndexCommandPort
import org.lgm.jobboard.jobposting.application.port.JobPostingIndexQueryPort
import org.lgm.jobboard.jobposting.infra.persistence.JobPostingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class JobPostingIndexBackfillRunner(
	private val jobPostingRepository: JobPostingRepository,
	private val jobPostingIndexQueryPort: JobPostingIndexQueryPort,
	private val jobPostingIndexCommandPort: JobPostingIndexCommandPort,
	@Value("\${app.search.backfill-on-start:false}")
	private val enabled: Boolean
) : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		if (!enabled) return

		val ids = jobPostingRepository.findAllIds()
		ids.forEach { id ->
			val doc = jobPostingIndexQueryPort.getIndexDocument(id) ?: return@forEach
			jobPostingIndexCommandPort.upsert(doc)
		}
	}
}