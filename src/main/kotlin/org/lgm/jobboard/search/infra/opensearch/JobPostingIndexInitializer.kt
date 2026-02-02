package org.lgm.jobboard.search.infra.opensearch

import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch.indices.CreateIndexRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class JobPostingIndexInitializer(
	private val client: OpenSearchClient,
	@Value("\${app.search.job-posting-index:job_postings}")
	private val indexName: String
) : ApplicationRunner {
	private val log = LoggerFactory.getLogger(javaClass)

	override fun run(args: ApplicationArguments) {
		ensureIndex()
	}

	/**
	 * 앱 실행 시 인덱스가 없으면 생성해 둠
	 */
	private fun ensureIndex() {
		val exists = client.indices().exists {  it.index(indexName) }.value()
		if (exists) {
			log.info("OpenSearch index already exists: {}", indexName)
			return
		}

		log.info("OpenSearch index not found. Creating index: {}", indexName)

		// CreateIndexRequest에 settings + mappings 정의
		val request = CreateIndexRequest.Builder()
			.index(indexName)
			// 실습/로컬 환경에서 replica=0 추천 (단일 노드)
			.settings { s ->
				s.numberOfShards("1")
					.numberOfReplicas("0")
			}
			// 매핑
			// - status 같은 필터링은 keyword로 빠르게 처리
			// - title/description은 text로 full-text 검색
			// - 필요한 필드는 정렬/집계 가능하도록 keyword/숫자 타입으로 지정
			.mappings { m ->
				m.properties("id") { p -> p.long_ { it } }
				m.properties("company") { p -> p.long_ { it }}

				// 검색 대상: text
				m.properties("title") { p ->
					p.text { t ->
						t.fields("keyword") { f -> f.keyword { k -> k.ignoreAbove(256) } }
					}
				}
				m.properties("description") { p -> p.text { it } }
				m.properties("companyName") { p ->
					p.text { t ->
						t.fields("keyword") { f -> f.keyword { k -> k.ignoreAbove(256) } }
					}
				}
				m.properties("location") { p ->
					p.text { t ->
						t.fields("keyword") { f -> f.keyword { k -> k.ignoreAbove(256) } }
					}
				}

				// 필터/집계용 필드: keyword
				m.properties("status") { p ->
					p.keyword { it }
				}
				m.properties("employmentType") { p ->
					p.keyword { it }
				}
				m.properties("companyIndustry") { p ->
					p.keyword { it }
				}

				// 숫자 필드
				m.properties("experienceMin") { p ->
					p.integer { it }
				}
				m.properties("experienceMax") { p ->
					p.integer { it }
				}

				// 스킬은 필터로 주로 쓰이므로 keyword 배열
				m.properties("skillNames") { p ->
					p.keyword { it }
				}

				// 시간 필드
				m.properties("updatedAt") { p ->
					p.date { it }
				}
			}
			.build()

		client.indices().create(request)

		log.info("OpenSearch index created: {}", indexName)
	}
}