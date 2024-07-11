package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.OAuthRequestPort
import com.celuveat.member.application.port.out.response.OAuthToken
import com.celuveat.member.application.port.out.response.OAuthUserInfoResponse
import com.celuveat.member.domain.OAuthServerType

@Adapter
class OAuthRequestAdapter(
    private val oAuthClients: Set<OAuthClient>,
) : OAuthRequestPort {

    override fun fetchOAuthToken(serverType: OAuthServerType, authCode: String): OAuthToken {
        val oAuthClient = getOAuthClient(serverType)
        return oAuthClient.fetchAccessToken(authCode)
    }

    override fun fetchUserInfo(serverType: OAuthServerType, accessToken: String): OAuthUserInfoResponse {
        val oAuthClient = getOAuthClient(serverType)
        return oAuthClient.fetchUserInfo(accessToken)
    }

    private fun getOAuthClient(serverType: OAuthServerType): OAuthClient {
        return oAuthClients.firstOrNull { it.matchSupportServer(serverType) }
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입: $serverType") // TODO 예외 분리
    }
}
