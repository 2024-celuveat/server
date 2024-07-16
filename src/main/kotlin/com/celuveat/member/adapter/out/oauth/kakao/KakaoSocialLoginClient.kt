package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.kakao.response.KakaoSocialLoginToken
import com.celuveat.member.adapter.out.oauth.response.SocialLoginInfoResponse
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component

@Component
class KakaoSocialLoginClient(
    private val kakaoSocialLoginProperty: KakaoSocialLoginProperty,
    private val kakaoApiClient: KakaoApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.KAKAO
    }

    override fun fetchMember(authCode: String): Member {
        val socialLoginToken = fetchAccessToken(authCode)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String): KakaoSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoSocialLoginProperty.clientId,
            "redirect_uri" to kakaoSocialLoginProperty.redirectUri,
            "code" to authCode,
            "client_secret" to kakaoSocialLoginProperty.clientSecret
        )
        return kakaoApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): SocialLoginInfoResponse {
        val kakaoMemberInfo = kakaoApiClient.fetchMemberInfo("Bearer $accessToken")
        return kakaoMemberInfo.toSocialLoginInfoResponse()
    }
}
