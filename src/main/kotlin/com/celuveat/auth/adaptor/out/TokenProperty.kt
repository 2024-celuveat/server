package com.celuveat.auth.adaptor.out

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")  // TODO application 파일에 추가
data class TokenProperty(
    val secretKey: String,
    val accessTokenExpirationMillis: Long
)
