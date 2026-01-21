package org.lgm.jobboard.jobposting.infra.persistence

import jakarta.persistence.*
import org.lgm.jobboard.common.jpa.BaseEntity
import org.lgm.jobboard.company.infra.persistence.CompanyEntity
import org.lgm.jobboard.jobposting.domain.EmploymentType
import org.lgm.jobboard.jobposting.domain.JobPostingStatus

@Entity
@Table(
	name = "job_posting",
	indexes = [
		Index(name = "idx_job_posting_company_id", columnList = "company_id"),
		Index(name = "idx_job_posting_status", columnList = "status")
	]
)
class JobPostingEntity(
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	var company: CompanyEntity,

	@Column(name = "title", nullable = false, length = 300)
	var title: String,

	@Lob
	@Column(name = "description", nullable = false)
	var description: String,

	@Enumerated(EnumType.STRING)
	@Column(name = "employment_type", nullable = false, length = 50)
	var employmentType: EmploymentType,

	@Column(name = "experience_min", nullable = false)
	var experienceMin: Int = 0,

	@Column(name = "experience_max")
	var experienceMax: Int? = null,

	@Column(name = "location", nullable = false, length = 200)
	var location: String,

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	var status: JobPostingStatus = JobPostingStatus.OPEN
) : BaseEntity() {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_posting_seq_gen")
	@SequenceGenerator(
		name = "job_posting_seq_gen",
		sequenceName = "public.job_posting_id_seq",
		allocationSize = 50
	)
	@Column(name = "id")
	var id: Long? = null
		protected set
}