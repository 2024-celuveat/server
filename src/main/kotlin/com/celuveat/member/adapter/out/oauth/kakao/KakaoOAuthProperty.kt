package com.celuveat.member.adapter.out.oauth.kakao

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.kakao")
data class KakaoOAuthProperty(
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>,
    val adminKey: String,
)
