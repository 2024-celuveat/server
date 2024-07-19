package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.SocialLoginType

interface GetSocialLoginUrlUseCase {

    fun getSocialLoginUrl(redirectUrl: String, socialLoginType: SocialLoginType): String
}
