package com.celuveat.member.adapter.out.oauth.config

import com.celuveat.member.adapter.out.oauth.kakao.KakaoApiClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Configuration
class SocialLoginApiClientConfig {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun kakaoApiClient(): KakaoApiClient {
        return createHttpInterface(KakaoApiClient::class.java)
    }

    private fun <T> createHttpInterface(clazz: Class<T>): T {
        val restClient = buildRestClientWithStatusHandler()
        return HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(restClient)
        ).build().createClient(clazz)
    }

    private fun buildRestClientWithStatusHandler(): RestClient {
        val restClient = RestClient.builder()
            .defaultStatusHandler(
                { status: HttpStatusCode -> status.is4xxClientError },
                { _, response ->
                    log.error("Client Error Code={}", response.statusCode)
                    log.error("Client Error Message={}", String(response.body.readAllBytes()))
                })
            .defaultStatusHandler(
                { status: HttpStatusCode -> status.is5xxServerError },
                { _, response ->
                    log.error("External Api Server Error Code={}", response.statusCode)
                    log.error("External Api Server Error Message={}", String(response.body.readAllBytes()))
                })
            .build()
        return restClient
    }
}
