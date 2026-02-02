package org.lgm.jobboard.company.infra.persistence

import jakarta.persistence.*
import org.lgm.jobboard.common.jpa.BaseEntity

@Entity
@Table(name = "company")
class CompanyEntity(
	@Column(name = "name", nullable = false, length = 200)
	var name: String,

	@Column(name = "website", length = 500)
	var website: String? = null,

	@Column(name = "industry", length = 100)
	var industry: String? = null
) : BaseEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq_gen")
	@SequenceGenerator(
		name = "company_seq_gen",
		sequenceName = "public.company_id_seq",
		allocationSize = 1
	)
	@Column(name = "id")
	var id: Long? = null
		protected set
}