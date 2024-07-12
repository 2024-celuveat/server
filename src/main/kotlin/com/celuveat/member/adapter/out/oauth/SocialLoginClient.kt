package com.celuveat.member.adapter.out.oauth

import com.celuveat.member.application.port.out.response.SocialLoginInfoResponse
import com.celuveat.member.application.port.out.response.SocialLoginToken
import com.celuveat.member.domain.SocialLoginType

interface SocialLoginClient {
    fun isSupports(socialLoginType: SocialLoginType): Boolean
    fun fetchAccessToken(authCode: String): SocialLoginToken
    fun fetchMemberInfo(accessToken: String): SocialLoginInfoResponse
}
