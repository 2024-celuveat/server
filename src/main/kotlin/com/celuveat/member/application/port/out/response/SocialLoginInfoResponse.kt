package com.celuveat.member.application.port.out.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.member.domain.SocialLoginType

data class SocialLoginInfoResponse(
    val id: String,
    val nickname: String,
    val profileImage: String,
    val serverType: SocialLoginType,
) {

    fun toMember(): Member {
        return Member(
            nickname = nickname,
            profileImageUrl = profileImage,
            socialIdentifier = SocialIdentifier(
                serverType = serverType,
                oAuthId = id,
            )
        )
    }
}
