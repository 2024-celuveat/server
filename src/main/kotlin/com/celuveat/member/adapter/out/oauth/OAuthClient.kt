package com.celuveat.member.adapter.out.oauth

import com.celuveat.member.application.port.out.response.OAuthToken
import com.celuveat.member.application.port.out.response.OAuthUserInfoResponse
import com.celuveat.member.domain.OAuthServerType

interface OAuthClient {
    fun matchSupportServer(serverType: OAuthServerType): Boolean
    fun fetchAccessToken(authCode: String): OAuthToken
    fun fetchUserInfo(accessToken: String): OAuthUserInfoResponse
}
