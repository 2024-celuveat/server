package com.celuveat.member.adapter.out.oauth.google

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.google")
data class GoogleSocialLoginProperty(
    val allowedRedirectUris: Set<String>,
    val clientId: String,
    val clientSecret: String,
    val scope: List<String>,
    val authorizationUrl: String = "https://accounts.google.com/o/oauth2/v2/auth",
)
