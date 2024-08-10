package com.celuveat.member.application.port.out

import com.celuveat.member.domain.SocialLoginType

interface ReadSocialLoginUrlPort {
    fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        requestOrigin: String,
    ): String
}
