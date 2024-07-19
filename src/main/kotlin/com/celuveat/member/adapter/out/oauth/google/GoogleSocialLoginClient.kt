package com.celuveat.member.adapter.out.oauth.google

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.google.response.GoogleMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.google.response.GoogleSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class GoogleSocialLoginClient(
    private val googleSocialLoginProperty: GoogleSocialLoginProperty,
    private val googleApiClient: GoogleApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.GOOGLE
    }

    override fun fetchMember(authCode: String, redirectUrl: String): Member {
        val socialLoginToken = fetchAccessToken(authCode, redirectUrl)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String, redirectUrl: String): GoogleSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to googleSocialLoginProperty.clientId,
            "client_secret" to googleSocialLoginProperty.clientSecret,
            "code" to authCode,
            "redirect_uri" to redirectUrl,
        )
        return googleApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): GoogleMemberInfoResponse {
        return googleApiClient.fetchMemberInfo("Bearer $accessToken")
    }

    override fun getSocialLoginUrl(redirectUrl: String): String {
        return UriComponentsBuilder
            .fromHttpUrl(googleSocialLoginProperty.authorizationUrl)
            .queryParam("client_id", googleSocialLoginProperty.clientId)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("response_type", "code")
            .queryParam("scope", googleSocialLoginProperty.scope.joinToString(","))
            .build()
            .toUriString()
    }
}
