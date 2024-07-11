package com.celuveat.member.application.port.out

import com.celuveat.member.domain.OAuthServerType
import com.celuveat.member.application.port.out.response.OAuthToken
import com.celuveat.member.application.port.out.response.OAuthUserInfoResponse

interface OAuthRequestPort {

    fun fetchOAuthToken(serverType: OAuthServerType, authCode: String): OAuthToken
    fun fetchUserInfo(serverType: OAuthServerType, accessToken: String): OAuthUserInfoResponse
}
