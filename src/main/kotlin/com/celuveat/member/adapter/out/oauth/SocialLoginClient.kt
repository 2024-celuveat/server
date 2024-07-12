package com.celuveat.member.adapter.out.oauth

import com.celuveat.member.adapter.out.oauth.response.SocialLoginInfoResponse
import com.celuveat.member.adapter.out.oauth.response.SocialLoginToken
import com.celuveat.member.domain.SocialLoginType

interface SocialLoginClient {
    fun isSupports(socialLoginType: SocialLoginType): Boolean
    fun fetchAccessToken(authCode: String): SocialLoginToken
    fun fetchMemberInfo(accessToken: String): SocialLoginInfoResponse
}
