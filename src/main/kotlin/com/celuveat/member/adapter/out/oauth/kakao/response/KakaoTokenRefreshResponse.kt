package com.celuveat.member.adapter.out.oauth.kakao.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

// https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#refresh-token-response
@JsonNaming(value = SnakeCaseStrategy::class)
data class KakaoTokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Int,
    val tokenType: String,
    val refreshTokenExpiresIn: Int?,
)
