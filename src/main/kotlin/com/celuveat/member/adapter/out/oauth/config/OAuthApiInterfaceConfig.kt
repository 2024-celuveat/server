package com.celuveat.member.adapter.out.oauth.config

import com.celuveat.member.adapter.out.oauth.kakao.KakaoApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Configuration
class OAuthApiClientInterfaceConfig {

    @Bean
    fun kakaoApiClient(): KakaoApiClient {
        return createHttpInterface(KakaoApiClient::class.java)
    }

    private fun <T> createHttpInterface(clazz: Class<T>): T {
        return HttpServiceProxyFactory.builderFor(
            WebClientAdapter.create(WebClient.create())
        ).build().createClient(clazz)
    }
}
