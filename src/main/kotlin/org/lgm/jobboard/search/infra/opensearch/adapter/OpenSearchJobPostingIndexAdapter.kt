package org.lgm.jobboard.search.infra.opensearch.adapter

import org.lgm.jobboard.jobposting.application.port.JobPostingIndexCommandPort
import org.lgm.jobboard.jobposting.application.query.JobPostingIndexDocument
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch.core.DeleteRequest
import org.opensearch.client.opensearch.core.UpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OpenSearchJobPostingIndexAdapter(
	private val client: OpenSearchClient,
	@Value("\${app.search.job-posting-index:job_postings}")
	private val indexName: String   // 운영에선 환경별로 인덱스 prefix 두기
) : JobPostingIndexCommandPort {
	private val log = LoggerFactory.getLogger(javaClass)

	override fun upsert(document: JobPostingIndexDocument) {
		// 문서는 index 안에 들어가며, 고유 식별자인 _id로 구분
		// jobPostingId를 그대로 _id로 사용 = 동일 공고는 항상 같은 문서를 갱신
		val request: UpdateRequest<JobPostingIndexDocument, JobPostingIndexDocument> =
			UpdateRequest.Builder<JobPostingIndexDocument, JobPostingIndexDocument>()
				.index(indexName)
				.id(document.id.toString())
				.doc(document)  // 업데이트에 사용할 문서 (부분/전체 모두 가능)
				.docAsUpsert(true)  // 문서가 없으면 doc을 새 문서로 삽입
				.build()

		val response = client.update(request, JobPostingIndexDocument::class.java)

		log.info("OpenSearch upsert OK. index={}, id={}, result={}", indexName, document.id, response.result())
	}

	override fun delete(jobPostingId: Long) {
		// index + _id로 문서를 제거
		// DB 삭제 이벤트가 오면 인덱스도 동기화해야 유령 검색 결과 현상이 발생하지 않음
		val request = DeleteRequest.Builder()
			.index(indexName)
			.id(jobPostingId.toString())
			.build()

		val response = client.delete(request)
		log.info("OpenSearch delete OK. index={}, id={}, result={}", indexName, jobPostingId, response.result())
	}
}