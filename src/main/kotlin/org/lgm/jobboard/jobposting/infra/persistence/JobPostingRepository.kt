package org.lgm.jobboard.jobposting.infra.persistence

import org.lgm.jobboard.jobposting.domain.type.JobPostingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JobPostingRepository : JpaRepository<JobPostingEntity, Long> {
	fun findAllByStatus(status: JobPostingStatus): List<JobPostingEntity>

	@Query(
		"""
		select jp
		from JobPostingEntity jp
		join fetch jp.company c
		where jp.id = :id
		"""
	)
	fun findByIdWithCompany(@Param("id") id: Long): JobPostingEntity?
}