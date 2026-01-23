package org.lgm.jobboard.jobposting.domain.model

/**
 * 경력 범위 규칙 값 객체
 */
data class ExperienceRange(
	val min: Int,
	val max: Int?
) {
	init {
		require(min >= 0) { "experienceMin must be >= 0" }
		if (max != null) require(max >= min) { "experienceMax must be >= experienceMin" }
	}
}
