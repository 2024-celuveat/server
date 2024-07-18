package com.celuveat.member.domain

data class SocialIdentifier(
    val serverType: SocialLoginType,
    val socialId: String,
)
