package com.celuveat.auth.adapter.out

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class TokenProperty(
    val secretKey: String,
    val accessTokenExpirationMillis: Long,
)
