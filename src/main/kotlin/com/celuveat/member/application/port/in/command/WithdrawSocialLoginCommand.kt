package com.celuveat.member.application.port.`in`.command

import com.celuveat.member.domain.SocialLoginType

data class WithdrawSocialLoginCommand(
    val memberId: Long,
    val authCode: String,
    val socialLoginType: SocialLoginType,
    val requestOrigin: String,
)
