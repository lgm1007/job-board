package org.lgm.jobboard.outbox.infra.persistence

import jakarta.persistence.*
import org.lgm.jobboard.common.jpa.BaseEntity
import org.lgm.jobboard.outbox.domain.type.AggregateType
import org.lgm.jobboard.outbox.domain.type.EventType
import org.lgm.jobboard.outbox.domain.type.OutboxStatus
import java.time.OffsetDateTime

@Entity
@Table(
	name = "outbox_event",
	indexes = [
		Index(name = "idx_outbox_status_available", columnList = "status, available_at"),
		Index(name = "idx_outbox_aggregate", columnList = "aggregate_type, aggregate_id")
	]
)
class OutboxEventEntity(
	@Enumerated(EnumType.STRING)
	@Column(name = "aggregate_type", nullable = false, length = 50)
	var aggregateType: AggregateType,

	@Column(name = "aggregate_id", nullable = false)
	var aggregateId: Long,

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", nullable = false, length = 50)
	var eventType: EventType,

	// jsonb 컬럼이지만 String으로 우선 처리
	@Column(name = "payload", nullable = false, columnDefinition = "jsonb")
	var payload: String,

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	var status: OutboxStatus = OutboxStatus.PENDING,

	@Column(name = "attempts", nullable = false)
	var attempts: Int = 0,

	@Column(name = "available_at", nullable = false)
	var availableAt: OffsetDateTime = OffsetDateTime.now(),

	@Lob
	@Column(name = "last_error")
	var lastError: String? = null
) : BaseEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outbox_event_seq_gen")
	@SequenceGenerator(
		name = "outbox_event_seq_gen",
		sequenceName = "public.outbox_event_id_seq",
		allocationSize = 50
	)
	@Column(name = "id")
	var id: Long? = null
		protected set

	fun markProcessing() {
		status = OutboxStatus.PROCESSING
	}

	fun markDone() {
		status = OutboxStatus.DONE
	}

	fun markFailed(errorMessage: String, nextAvailableAt: OffsetDateTime) {
		status = OutboxStatus.FAILED
		attempts += 1
		lastError = errorMessage
		availableAt = nextAvailableAt
	}

	fun requeue(nextAvailableAt: OffsetDateTime) {
		status = OutboxStatus.PENDING
		availableAt = nextAvailableAt
	}
}