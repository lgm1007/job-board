package org.lgm.jobboard.jobposting.infra.persistence

import org.lgm.jobboard.jobposting.domain.type.JobPostingStatus
import org.lgm.jobboard.jobposting.infra.persistence.pk.JobPostingSkillId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JobPostingSkillRepository : JpaRepository<JobPostingSkillEntity, JobPostingSkillId> {
	@Modifying
	@Query(
		"""
		delete
		from JobPostingSkillEntity jps
		where jps.jobPosting.id = :jobPostingId
		"""
	)
	fun deleteAllByJobPostingId(@Param("jobPostingId") jobPostingId: Long): Int

	@Query(
		"""
		select jps
		from JobPostingSkillEntity jps
		join fetch jps.skill s
		where jps.jobPosting.id = :jobPostingId
		"""
	)
	fun findAllByJobPostingIdWithSkill(@Param("jobPostingId") jobPostingId: Long): List<JobPostingSkillEntity>

	@Query(
		"""
		select jps
		from JobPostingSkillEntity jps
		join fetch jps.skill s
		where jps.jobPosting.id in :jobPostingIds
		"""
	)
	fun findAllByJobPostingIdsWithSkill(@Param("jobPostingIds") jobPostingIds: List<Long>): List<JobPostingSkillEntity>

	@Query(
	"""
		select distinct jp.id
		from JobPostingSkillEntity jps
		join jps.jobPosting jp
		join jps.skill s
		where (:companyId is null or jp.company.id = :companyId)
			and (:status is null or jp.status = :status)
			and s.name = :skill
	"""
	)
	fun searchIdsBySkill(
		@Param("companyId") companyId: Long?,
		@Param("status") status: JobPostingStatus?,
		@Param("skill") skill: String,
		pageable: Pageable
	): Page<Long>
}