package com.celuveat.member.application.port.out

import com.celuveat.member.domain.SocialLoginType

interface WithdrawSocialMemberPort {
    fun withdraw(
        refreshToken: String,
        socialLoginType: SocialLoginType,
        requestOrigin: String,
    )
}
