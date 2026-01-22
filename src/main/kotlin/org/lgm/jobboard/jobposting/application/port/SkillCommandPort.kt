package org.lgm.jobboard.jobposting.application.port

interface SkillCommandPort {
	fun upsertAll(skills: Set<String>): Set<String>
}