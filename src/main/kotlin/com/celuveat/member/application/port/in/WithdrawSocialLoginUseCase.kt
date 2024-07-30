package com.celuveat.member.application.port.`in`

import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand

interface WithdrawSocialLoginUseCase {
    fun withdraw(command: WithdrawSocialLoginCommand)
}
