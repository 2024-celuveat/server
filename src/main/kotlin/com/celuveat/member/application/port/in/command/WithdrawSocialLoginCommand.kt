package com.celuveat.member.application.port.`in`.command

data class WithdrawSocialLoginCommand(
    val memberId: Long,
    val requestOrigin: String,
)
