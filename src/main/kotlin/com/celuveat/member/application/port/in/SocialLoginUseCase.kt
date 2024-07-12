package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.OAuthServerType

interface SocialLoginUseCase {

    fun login(serverType: OAuthServerType, authCode: String): Long
}
