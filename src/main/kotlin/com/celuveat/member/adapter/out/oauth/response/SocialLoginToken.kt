package com.celuveat.member.adapter.out.oauth.response

data class SocialLoginToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val tokenType: String,
    val refreshTokenExpiresIn: Int
)
