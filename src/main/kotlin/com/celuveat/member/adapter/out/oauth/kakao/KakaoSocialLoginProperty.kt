package com.celuveat.member.adapter.out.oauth.kakao

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.kakao")
data class KakaoSocialLoginProperty(
    val allowedRedirectUris: Set<String>,
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>,
    val adminKey: String,
    val authorizationUrl: String = "https://kauth.kakao.com/oauth/authorize",
)
