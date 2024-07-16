package com.celuveat.member.adapter.out.oauth.naver

import com.celuveat.member.adapter.out.oauth.SocialLoginClient
import com.celuveat.member.adapter.out.oauth.naver.response.NaverMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.naver.response.NaverSocialLoginToken
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Component

@Component
class NaverSocialLoginClient(
    private val naverSocialLoginProperty: NaverSocialLoginProperty,
    private val naverApiClient: NaverApiClient,
) : SocialLoginClient {

    override fun isSupports(socialLoginType: SocialLoginType): Boolean {
        return socialLoginType == SocialLoginType.NAVER
    }

    override fun fetchMember(authCode: String): Member {
        val socialLoginToken = fetchAccessToken(authCode)
        return fetchMemberInfo(socialLoginToken.accessToken).toMember()
    }

    private fun fetchAccessToken(authCode: String): NaverSocialLoginToken {
        val tokenRequestBody = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to naverSocialLoginProperty.clientId,
            "client_secret" to naverSocialLoginProperty.clientSecret,
            "code" to authCode,
            "state" to naverSocialLoginProperty.state
        )
        return naverApiClient.fetchToken(tokenRequestBody)
    }

    private fun fetchMemberInfo(accessToken: String): NaverMemberInfoResponse {
        return naverApiClient.fetchMemberInfo("Bearer $accessToken")
    }
}
