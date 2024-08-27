package com.celuveat.member.application.port.`in`

import com.celuveat.member.application.port.`in`.command.UpdateProfileCommand

interface UpdateProfileUseCase {
    fun updateProfile(command: UpdateProfileCommand)
}
