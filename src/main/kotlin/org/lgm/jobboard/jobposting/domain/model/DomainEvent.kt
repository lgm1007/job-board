package org.lgm.jobboard.jobposting.domain.model

import java.time.OffsetDateTime

interface DomainEvent {
	val occurredAt: OffsetDateTime
}