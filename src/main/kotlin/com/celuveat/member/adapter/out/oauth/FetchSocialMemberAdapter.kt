package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

@Adapter
class FetchSocialMemberAdapter(
    private val socialLoginClients: Set<SocialLoginClient>,
) : FetchSocialMemberPort {

    override fun fetchMember(serverType: SocialLoginType, authCode: String): Member {
        val oAuthClient = getOAuthClient(serverType)
        val fetchToken = oAuthClient.fetchAccessToken(authCode)
        val fetchUserInfo = oAuthClient.fetchUserInfo(fetchToken.accessToken)
        return fetchUserInfo.toMember()
    }

    private fun getOAuthClient(serverType: SocialLoginType): SocialLoginClient {
        return socialLoginClients.firstOrNull { it.matchSupportServer(serverType) }
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입: $serverType") // TODO 예외 분리
    }
}
