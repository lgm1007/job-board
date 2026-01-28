package org.lgm.jobboard.outbox.infra.persistence

import jakarta.persistence.LockModeType
import org.lgm.jobboard.outbox.domain.type.OutboxStatus
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.OffsetDateTime

interface OutboxEventRepository : JpaRepository<OutboxEventEntity, Long> {
	@Query(
		"""
		select oe
		from OutboxEventEntity oe
		where oe.status = :status
		and oe.availableAt <= :now
		order by oe.createdAt asc
		"""
	)
	fun findAvailableEvents(
		@Param("status") status: OutboxStatus,
		@Param("now") now: OffsetDateTime,
		pageable: PageRequest
	): List<OutboxEventEntity>

	// 활성화 일자인 PENDING 상태의 이벤트 페이징 조회
	@Query(
		"""
		select oe.id
		from OutboxEventEntity oe
		where oe.status = 'PENDING'
			and oe.availableAt <= :now
		order by oe.createdAt asc
		"""
	)
	fun findDueIds(@Param("now") now: OffsetDateTime, pageable: Pageable): List<Long>

	// 멀티 인스턴스 확장 고려 비관적 락
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(
		"""
		select oe
		from OutboxEventEntity oe
		where oe.id = :id
		"""
	)
	fun lockById(@Param("id") id: Long): OutboxEventEntity?
}