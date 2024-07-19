package com.celuveat.member.application.port.out

import com.celuveat.member.domain.SocialLoginType

interface GetSocialLoginUrlPort {

    fun getSocialLoginUrl(redirectUrl: String, socialLoginType: SocialLoginType): String
}
