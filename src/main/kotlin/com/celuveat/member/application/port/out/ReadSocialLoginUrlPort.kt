package com.celuveat.member.application.port.out

import com.celuveat.member.domain.SocialLoginType

interface ReadSocialLoginUrlPort {
    fun readSocialLoginUrl(
        socialLoginType: SocialLoginType,
        requestOrigin: String,
    ): String
}
