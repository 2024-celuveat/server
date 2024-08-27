package com.celuveat.member.application.port.`in`.result

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

data class MemberResult(
    val id: Long = 0,
    val nickname: String,
    val profileImageUrl: String?,
    val email: String,
    val serverType: SocialLoginType,
    val socialId: String,
) {
    companion object {
        fun from(member: Member): MemberResult {
            return MemberResult(
                id = member.id,
                nickname = member.nickname,
                profileImageUrl = member.profileImageUrl,
                email = member.email,
                serverType = member.socialIdentifier.serverType,
                socialId = member.socialIdentifier.socialId,
            )
        }
    }
}
