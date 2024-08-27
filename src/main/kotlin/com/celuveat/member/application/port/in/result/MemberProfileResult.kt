package com.celuveat.member.application.port.`in`.result

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

data class MemberProfileResult(
    val id: Long = 0,
    val nickname: String,
    val profileImageUrl: String?,
    val email: String,
    val serverType: SocialLoginType,
    val socialId: String,
    val interestedCount: Int,
    val reviewCount: Int,
) {
    companion object {
        fun of(
            member: Member,
            interestedCount: Int,
            reviewCount: Int,
        ): MemberProfileResult {
            return MemberProfileResult(
                id = member.id,
                nickname = member.nickname,
                profileImageUrl = member.profileImageUrl,
                email = member.email,
                serverType = member.socialIdentifier.serverType,
                socialId = member.socialIdentifier.socialId,
                interestedCount = interestedCount,
                reviewCount = reviewCount,
            )
        }
    }
}
