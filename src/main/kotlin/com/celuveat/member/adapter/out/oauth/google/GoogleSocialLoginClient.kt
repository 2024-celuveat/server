package com.celuveat.member.adapter.out.oauth.google

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.google.response.GoogleMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.google.response.GoogleSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component

@Component
class GoogleSocialLoginClient(
    private val googleSocialLoginProperty: GoogleSocialLoginProperty,
    private val googleApiClient: GoogleApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.GOOGLE
    }

    override fun fetchMember(authCode: String): Member {
        val socialLoginToken = fetchAccessToken(authCode)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String): GoogleSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to googleSocialLoginProperty.clientId,
            "client_secret" to googleSocialLoginProperty.clientSecret,
            "code" to authCode,
            "redirect_uri" to googleSocialLoginProperty.redirectUri,
        )
        return googleApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): GoogleMemberInfoResponse {
        return googleApiClient.fetchMemberInfo("Bearer $accessToken")
    }
}
