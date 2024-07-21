package com.celuveat.member.application.port.`in`.command

import com.celuveat.member.domain.SocialLoginType

data class SocialLoginCommand(
    val socialLoginType: SocialLoginType,
    val authCode: String,
    val requestOrigin: String,
)
