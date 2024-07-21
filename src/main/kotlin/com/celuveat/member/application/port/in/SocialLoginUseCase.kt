package com.celuveat.member.application.port.`in`

import com.celuveat.member.application.port.`in`.command.SocialLoginCommand

interface SocialLoginUseCase {

    fun login(command: SocialLoginCommand): Long
}
