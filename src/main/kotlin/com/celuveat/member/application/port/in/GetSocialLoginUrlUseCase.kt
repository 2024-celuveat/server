package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.SocialLoginType

interface GetSocialLoginUrlUseCase {
    fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        redirectUrl: String,
    ): String
}
