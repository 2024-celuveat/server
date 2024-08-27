package com.celuveat.member.adapter.`in`.rest.request

import com.celuveat.member.application.port.`in`.command.UpdateProfileCommand

data class UpdateProfileRequest(
    val nickname: String,
    val profileImageUrl: String,
) {
    fun toCommand(memberId: Long): UpdateProfileCommand {
        return UpdateProfileCommand(
            memberId = memberId,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
        )
    }
}
