package com.celuveat.member.application.port.`in`

import com.celuveat.member.domain.SocialLoginType

interface SocialLoginUseCase {

    fun login(serverType: SocialLoginType, authCode: String): Long
}
