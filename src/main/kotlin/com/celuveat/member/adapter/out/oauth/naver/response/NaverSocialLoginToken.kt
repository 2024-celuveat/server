package com.celuveat.member.adapter.out.oauth.naver.response

data class NaverSocialLoginToken(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val error: String,
    val errorDescription: String,
)
