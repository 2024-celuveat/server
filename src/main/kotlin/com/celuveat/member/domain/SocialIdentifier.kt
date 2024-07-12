package com.celuveat.member.domain

data class SocialIdentifier(
    val serverType: OAuthServerType,
    val oAuthId: String,
)
