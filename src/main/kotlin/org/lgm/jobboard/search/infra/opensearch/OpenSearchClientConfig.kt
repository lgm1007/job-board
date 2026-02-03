package org.lgm.jobboard.search.infra.opensearch

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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

		val mapper = ObjectMapper()
			.registerModule(JavaTimeModule())
			// ISO-8601 문자열로 직렬화하도록 (timestamp 배열/숫자 방지)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

		// OpenSearch Java Client는 Transport 위에서 동작
		// Transport: client + JSON Mapper 연결
		val transport: OpenSearchTransport = RestClientTransport(
			restClient,
			JacksonJsonpMapper(mapper) // OpenSearch 요청/응답 바디 직렬화/역직렬화 담당
		)

		return OpenSearchClient(transport)
	}
}