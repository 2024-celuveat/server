package com.celuveat.member.adapter.out.oauth.naver

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.naver")
data class NaverSocialLoginProperty(
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>,
    val state: String,
    val authorizationUrl: String = "https://nid.naver.com/oauth2.0/authorize",
)
