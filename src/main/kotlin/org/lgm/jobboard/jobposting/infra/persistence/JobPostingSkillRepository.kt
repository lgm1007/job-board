package org.lgm.jobboard.jobposting.infra.persistence

import org.lgm.jobboard.jobposting.infra.persistence.pk.JobPostingSkillId
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
}