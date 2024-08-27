package com.celuveat.member.application.port.`in`.command

data class UpdateProfileCommand(
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String,
)
