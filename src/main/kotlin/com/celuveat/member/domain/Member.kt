package com.celuveat.member.domain

data class Member(
    val id: Long = 0,
    val nickname: String,
    val profileImageUrl: String?,
    val socialIdentifier: SocialIdentifier,
)
