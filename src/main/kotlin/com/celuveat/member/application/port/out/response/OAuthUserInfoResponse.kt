package com.celuveat.member.application.port.out.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType

data class OAuthUserInfoResponse(
    val id: String,
    val nickname: String,
    val profileImage: String,
    val serverType: OAuthServerType,
) {

    fun toMember(): Member {
        return Member(
            nickname = nickname,
            profileImageUrl = profileImage,
            serverType = serverType,
            oAuthId = id,
        )
    }
}
