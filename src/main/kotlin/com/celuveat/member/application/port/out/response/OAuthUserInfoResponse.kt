package com.celuveat.member.application.port.out.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType
import com.celuveat.member.domain.SocialIdentifier

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
            socialIdentifier = SocialIdentifier(
                serverType = serverType,
                oAuthId = id,
            )
        )
    }
}
