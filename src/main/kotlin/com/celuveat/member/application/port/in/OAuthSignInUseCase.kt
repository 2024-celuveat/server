package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.OAuthServerType

interface OAuthSignInUseCase {

    fun signIn(serverType: OAuthServerType, authCode: String): Long
}
