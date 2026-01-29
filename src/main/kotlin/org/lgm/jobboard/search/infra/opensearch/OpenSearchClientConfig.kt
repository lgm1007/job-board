package org.lgm.jobboard.search.infra.opensearch

import org.apache.http.HttpHost
import org.opensearch.client.RestClient
import org.opensearch.client.json.jackson.JacksonJsonpMapper
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.transport.OpenSearchTransport
import org.opensearch.client.transport.rest_client.RestClientTransport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenSearchClientConfig {
	@Bean
	fun openSearchClient(): OpenSearchClient {
		// 로컬 기준
		val restClient = RestClient.builder(HttpHost("localhost", 9200, "http")).build()

		// OpenSearch Java Client는 Transport 위에서 동작
		// Transport: client + JSON Mapper 연결
		val transport: OpenSearchTransport = RestClientTransport(
			restClient,
			JacksonJsonpMapper() // OpenSearch 요청/응답 바디 직렬화/역직렬화 담당
		)

		return OpenSearchClient(transport)
	}
}