package org.lgm.jobboard.outbox.domain.type

enum class OutboxStatus(val description: String) {
	PENDING("대기중"),
	PROCESSING("진행중"),
	DONE("완료"),
	FAILED("실패")
}