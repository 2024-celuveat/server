package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.FetchOauthMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType

@Adapter
class FetchOauthMemberAdapter(
    private val oAuthClients: Set<OAuthClient>,
) : FetchOauthMemberPort {

    override fun fetchMember(serverType: OAuthServerType, authCode: String): Member {
        val oAuthClient = getOAuthClient(serverType)
        val fetchToken = oAuthClient.fetchAccessToken(authCode)
        val fetchUserInfo = oAuthClient.fetchUserInfo(fetchToken.accessToken)
        return fetchUserInfo.toMember()
    }

    private fun getOAuthClient(serverType: OAuthServerType): OAuthClient {
        return oAuthClients.firstOrNull { it.matchSupportServer(serverType) }
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입: $serverType") // TODO 예외 분리
    }
}
