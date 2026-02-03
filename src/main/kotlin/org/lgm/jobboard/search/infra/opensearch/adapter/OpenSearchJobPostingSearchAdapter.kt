package org.lgm.jobboard.search.infra.opensearch.adapter

import org.lgm.jobboard.search.application.port.JobPostingSearchPort
import org.lgm.jobboard.search.application.query.JobPostingSearchCondition
import org.lgm.jobboard.search.application.query.SearchResult
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.FieldValue
import org.opensearch.client.opensearch._types.SortOrder
import org.opensearch.client.opensearch._types.query_dsl.Operator
import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch.core.SearchRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OpenSearchJobPostingSearchAdapter(
	private val client: OpenSearchClient,
	@Value("\${app.search.job-posting-index:job_postings}")
	private val indexName: String
) : JobPostingSearchPort {
	override fun search(
		condition: JobPostingSearchCondition,
		page: Int,
		size: Int
	): SearchResult {
		val safePage = page.coerceAtLeast(0)
		val safeSize = size.coerceIn(1, 100)
		val from = safePage * safeSize

		/*
		 * - bool query 사용
		 *   - must: 키워드(q) 기반 full-text 검색 (관련도 스코어 계산 대상)
		 *   - filter: 정확 매칭 필터 (스코어 영향 X, 캐시되기 쉬움)
		 *
		 * - q가 없으면 match_all + filter만 적용하는 형태
		 */
		val query = buildBoolQuery(condition)

		val request: SearchRequest = SearchRequest.Builder()
			.index(indexName)
			.from(from)
			.size(safeSize)
			.query(query)
			.sort { s ->
				/*
				 * 정렬:
				 * 검색은 관련도(score)가 중요, 최신순과 관련도와 섞기도 함
				 *
				 * - q가 있으면 score 우선
				 * - q가 없으면 updatedAt desc로 정렬
				 */
				if (!condition.q.isNullOrBlank()) {
					// score 정렬은 기본이므로 명시 안 해도 됨
					s.score { it.order(SortOrder.Desc) }
				} else {
					s.field { f ->
						f.field("updatedAt").order(SortOrder.Desc)
					}
				}
			}
			.source { src ->
				// 네트워크/응답량 최적화: 필요 없는 _source 전체는 끄고, id만 뽑아도 됨
				// id 필드만 뽑기 위해 source filtering 사용
				src.filter { f ->
					f.includes("id")
				}
			}
			.build()

		val response = client.search(request, Map::class.java)

		val ids = response.hits().hits().mapNotNull { hit ->
			// _source.id 또는 _id 둘 중 택1
			val source = hit.source() ?: return@mapNotNull null
			val id = (source["id"] as? Number)?.toLong() // 문서 id 필드 사용
			id
		}

		val total = response.hits().total()?.value() ?: ids.size.toLong()
		return SearchResult(ids = ids, total = total)
	}

	private fun buildBoolQuery(condition: JobPostingSearchCondition): Query {
		val q = condition.q?.trim().orEmpty()

		return Query.Builder().bool { b ->
			// must: 키워드 검색
			if (q.isNotBlank()) {
				/*
				 * - title/description/companyName 등 여러 필드에 동시에 검색 수행
				 * - operator=AND로 두면 단어 모두 포함
				 * - fields 각각 가중치를 주면 제목이 더 중요하게 랭킹됨
				 */
				b.must { m ->
					m.multiMatch { mm ->
						mm.query(q)
							.fields(
								"title^3",
								"companyName^2",
								"description"
							)
							.operator(Operator.And)
					}
				}
			} else {
				// q가 없으면 match_all로, filter만 적용
				b.must { m -> m.matchAll { it } }
			}

			// filter: 정확 매칭 필터
			condition.companyId?.let { cid ->
				b.filter { f ->
					f.term { t ->
						t.field("companyId").value(FieldValue.of(cid))
					}
				}
			}

			condition.status?.takeIf { it.isNotBlank() }?.let { stat ->
				b.filter { f ->
					f.term { t ->
						t.field("status").value(FieldValue.of(stat))
					}
				}
			}

			condition.skill?.trim()?.takeIf { it.isNotBlank() }?.let { sk ->
				// skillName은 키워드 배열: term이 잘 맞음
				b.filter { f ->
					f.term { t ->
						t.field("skillNames").value(FieldValue.of(sk))
					}
				}
			}

			b
		}.build()
	}
}