package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.SocialLoginType

interface ReadSocialLoginUrlUseCase {
    fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        requestOrigin: String,
    ): String
}
