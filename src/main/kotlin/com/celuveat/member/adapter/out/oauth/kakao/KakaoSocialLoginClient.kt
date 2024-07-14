package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.response.SocialLoginInfoResponse
import com.celuveat.member.adapter.out.oauth.response.SocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component

@Component
class KakaoSocialLoginClient(
    private val oAuthProperty: KakaoSocialLoginProperty,
    private val kakaoApiClient: KakaoApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.KAKAO
    }

    override fun fetchMember(authCode: String): Member {
        val socialLoginToken = fetchAccessToken(authCode)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String): SocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to oAuthProperty.clientId,
            "redirect_uri" to oAuthProperty.redirectUri,
            "code" to authCode,
            "client_secret" to oAuthProperty.clientSecret
        )
        return kakaoApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): SocialLoginInfoResponse {
        val kakaoMemberInfo = kakaoApiClient.fetchMemberInfo("Bearer $accessToken")
        return kakaoMemberInfo.toOAuthUserInfoResponse()
    }
}
