package org.lgm.jobboard.jobposting.infra.persistence

import jakarta.persistence.*
import org.lgm.jobboard.common.jpa.BaseEntity

@Entity
@Table(name = "skill")
class SkillEntity(
	@Column(name = "name", nullable = false, length = 100)
	var name: String
) : BaseEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_seq_gen")
	@SequenceGenerator(
		name = "skill_seq_gen",
		sequenceName = "public.skill_id_seq",
		allocationSize = 1
	)
	@Column(name = "id")
	var id: Long? = null
		protected set
}