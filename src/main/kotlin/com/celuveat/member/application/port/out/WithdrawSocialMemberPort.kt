package com.celuveat.member.application.port.out

import com.celuveat.member.domain.SocialLoginType

interface WithdrawSocialMemberPort {
    fun withdraw(
        authCode: String,
        socialLoginType: SocialLoginType,
        redirectUrl: String,
    )
}
