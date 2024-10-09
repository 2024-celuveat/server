package com.celuveat.member.adapter.out.oauth.google

import com.celuveat.common.utils.doesNotContain
import com.celuveat.common.utils.throwWhen
import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.google.response.GoogleMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.google.response.GoogleSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.member.exception.NotAllowedRedirectUriException
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

    override fun fetchMember(
        authCode: String,
        requestOrigin: String,
    ): Member {
        val redirectUrl = toRedirectUrl(requestOrigin)
        validateAllowedRedirectUrl(redirectUrl)
        val socialLoginToken = fetchAccessToken(authCode, redirectUrl)
        return fetchMemberInfo(socialLoginToken.accessToken)
            .toMember(socialLoginToken.refreshToken)
    }

    private fun validateAllowedRedirectUrl(redirectUrl: String) {
        val allowedRedirectUris = googleSocialLoginProperty.allowedRedirectUris
        throwWhen(allowedRedirectUris.doesNotContain(redirectUrl)) { NotAllowedRedirectUriException }
    }

    private fun fetchAccessToken(
        authCode: String,
        redirectUrl: String,
    ): GoogleSocialLoginToken {
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

    override fun getSocialLoginUrl(requestOrigin: String): String {
        return UriComponentsBuilder
            .fromHttpUrl(googleSocialLoginProperty.authorizationUrl)
            .queryParam("client_id", googleSocialLoginProperty.clientId)
            .queryParam("redirect_uri", toRedirectUrl(requestOrigin))
            .queryParam("response_type", "code")
            .queryParam("scope", googleSocialLoginProperty.scope.joinToString(" "))
            .build()
            .toUriString()
    }

    private fun toRedirectUrl(requestOrigin: String) = "$requestOrigin/oauth/google"

    override fun withdraw(
        refreshToken: String,
        requestOrigin: String,
    ) {
        val socialLoginToken = refreshToken(refreshToken)
        googleApiClient.withdraw(socialLoginToken.accessToken)
    }

    private fun refreshToken(
        refreshToken: String,
    ): GoogleSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "refresh_token",
            "client_id" to googleSocialLoginProperty.clientId,
            "client_secret" to googleSocialLoginProperty.clientSecret,
            "refresh_token" to refreshToken,
        )
        return googleApiClient.refreshToken(tokenRequestBody)
    }
}
