package com.celuveat.member.adapter.out.oauth.google

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.google")
data class GoogleSocialLoginProperty(
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>
)
