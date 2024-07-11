package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.OAuthClient
import com.celuveat.member.application.port.out.response.OAuthToken
import com.celuveat.member.application.port.out.response.OAuthUserInfoResponse
import com.celuveat.member.domain.OAuthServerType
import org.springframework.stereotype.Component

@Component
class KakaoOAuthClient(
    private val oAuthProperty: KakaoOAuthProperty,
    private val kakaoApiClient: KakaoApiClient,
) : OAuthClient {

    override fun matchSupportServer(serverType: OAuthServerType): Boolean {
        return serverType == OAuthServerType.KAKAO
    }

    override fun fetchAccessToken(authCode: String): OAuthToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to oAuthProperty.clientId,
            "redirect_uri" to oAuthProperty.redirectUri,
            "code" to authCode,
            "client_secret" to oAuthProperty.clientSecret
        )
        return kakaoApiClient.fetchToken(tokenRequestBody)
    }

    override fun fetchUserInfo(accessToken: String): OAuthUserInfoResponse {
        val kakaoOauthUserInfo = kakaoApiClient.fetchUserInfo("Bearer $accessToken")
        return kakaoOauthUserInfo.toOAuthUserInfoResponse()
    }
}
