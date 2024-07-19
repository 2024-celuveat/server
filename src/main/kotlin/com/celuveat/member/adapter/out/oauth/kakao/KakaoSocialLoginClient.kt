package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.kakao.response.KakaoMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.kakao.response.KakaoSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoSocialLoginClient(
    private val kakaoSocialLoginProperty: KakaoSocialLoginProperty,
    private val kakaoApiClient: KakaoApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.KAKAO
    }

    override fun fetchMember(authCode: String, redirectUrl: String): Member {
        val socialLoginToken = fetchAccessToken(authCode, redirectUrl)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String, redirectUrl: String): KakaoSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoSocialLoginProperty.clientId,
            "redirect_uri" to redirectUrl,
            "code" to authCode,
            "client_secret" to kakaoSocialLoginProperty.clientSecret
        )
        return kakaoApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): KakaoMemberInfoResponse {
        return kakaoApiClient.fetchMemberInfo("Bearer $accessToken")
    }

    override fun getSocialLoginUrl(redirectUrl: String): String {
        return UriComponentsBuilder
            .fromHttpUrl(kakaoSocialLoginProperty.authorizationUrl)
            .queryParam("client_id", kakaoSocialLoginProperty.clientId)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("response_type", "code")
            .queryParam("scope", kakaoSocialLoginProperty.scope.joinToString(","))
            .build()
            .toUriString()
    }
}
