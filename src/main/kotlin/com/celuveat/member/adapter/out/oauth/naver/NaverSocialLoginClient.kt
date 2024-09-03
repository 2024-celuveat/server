package com.celuveat.member.adapter.out.oauth.naver

import com.celuveat.common.utils.doesNotContain
import com.celuveat.common.utils.throwWhen
import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.naver.response.NaverMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.naver.response.NaverSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.member.exception.NotAllowedRedirectUriException
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class NaverSocialLoginClient(
    private val naverSocialLoginProperty: NaverSocialLoginProperty,
    private val naverApiClient: NaverApiClient,
) : SocialLoginClient {
    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.NAVER
    }

    override fun fetchMember(
        authCode: String,
        requestOrigin: String,
    ): Member {
        val redirectUrl = toRedirectUrl(requestOrigin)
        validateAllowedRedirectUrl(redirectUrl)
        val socialLoginToken = fetchAccessToken(authCode)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun validateAllowedRedirectUrl(redirectUrl: String) {
        val allowedRedirectUris = naverSocialLoginProperty.allowedRedirectUris
        throwWhen(allowedRedirectUris.doesNotContain(redirectUrl)) { NotAllowedRedirectUriException }
    }

    private fun fetchAccessToken(authCode: String): NaverSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to naverSocialLoginProperty.clientId,
            "client_secret" to naverSocialLoginProperty.clientSecret,
            "code" to authCode,
            "state" to naverSocialLoginProperty.state,
        )
        return naverApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): NaverMemberInfoResponse {
        return naverApiClient.fetchMemberInfo("Bearer $accessToken")
    }

    override fun getSocialLoginUrl(requestOrigin: String): String {
        return UriComponentsBuilder
            .fromHttpUrl(naverSocialLoginProperty.authorizationUrl)
            .queryParam("client_id", naverSocialLoginProperty.clientId)
            .queryParam("redirect_uri", "$requestOrigin/oauth/naver")
            .queryParam("response_type", "code")
            .queryParam("state", naverSocialLoginProperty.state)
            .build()
            .toUriString()
    }

    override fun withdraw(
        authCode: String,
        requestOrigin: String,
    ) {
        val accessToken = fetchAccessToken(authCode)
        val tokenRequestBody = mapOf(
            "client_id" to naverSocialLoginProperty.clientId,
            "client_secret" to naverSocialLoginProperty.clientSecret,
            "access_token" to accessToken.accessToken,
            "grant_type" to "delete",
        )
        naverApiClient.withdraw(tokenRequestBody)
    }

    private fun toRedirectUrl(requestOrigin: String) = "$requestOrigin/oauth/naver"
}
