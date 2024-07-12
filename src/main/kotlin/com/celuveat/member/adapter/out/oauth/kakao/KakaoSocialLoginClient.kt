package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.application.port.out.response.SocialLoginInfoResponse
import com.celuveat.member.application.port.out.response.SocialLoginToken
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component

@Component
class KakaoSocialLoginClient(
    private val oAuthProperty: KakaoSocialLoginProperty,
    private val kakaoApiClient: KakaoApiClient,
) : SocialLoginClient {

    override fun matchSupportServer(serverType: SocialLoginType): Boolean {
        return serverType == SocialLoginType.KAKAO
    }

    override fun fetchAccessToken(authCode: String): SocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to oAuthProperty.clientId,
            "redirect_uri" to oAuthProperty.redirectUri,
            "code" to authCode,
            "client_secret" to oAuthProperty.clientSecret
        )
        return kakaoApiClient.fetchToken(tokenRequestBody)
    }

    override fun fetchUserInfo(accessToken: String): SocialLoginInfoResponse {
        val kakaoOauthUserInfo = kakaoApiClient.fetchUserInfo("Bearer $accessToken")
        return kakaoOauthUserInfo.toOAuthUserInfoResponse()
    }
}
