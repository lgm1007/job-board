package org.lgm.jobboard.jobposting.infra.persistence

import org.lgm.jobboard.jobposting.domain.type.JobPostingStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

	@Query(
		"""
		select jp
		from JobPostingEntity jp
		join fetch jp.company c
		where jp.id in :ids
		"""
	)
	fun findAllByIdInWithCompany(@Param("ids") ids: List<Long>): List<JobPostingEntity>

	@Query(
		"""
		select jp.id
		from JobPostingEntity jp
		where (:companyId is null or jp.company.id = :companyId)
			and (:status is null or jp.status = :status)
		"""
	)
	fun searchIds(
		@Param("companyId") companyId: Long?,
		@Param("status") status: JobPostingStatus?,
		pageable: Pageable
	): Page<Long>

	@Query(
		"""
		select jp.id
		from JobPostingEntity jp
		order by jp.id asc
		"""
	)
	fun findAllIds(): List<Long>
}