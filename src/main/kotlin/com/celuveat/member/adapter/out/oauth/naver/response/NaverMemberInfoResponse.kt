package com.celuveat.member.adapter.out.oauth.naver.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class NaverMemberInfoResponse(
    private val resultcode: String,
    private val message: String,
    private val response: Response,
) {
    fun toMember(refreshToken: String): Member {
        return Member(
            nickname = response.nickname,
            profileImageUrl = response.profileImage,
            email = response.email,
            socialIdentifier = SocialIdentifier(
                serverType = SocialLoginType.NAVER,
                socialId = response.id,
                refreshToken = refreshToken,
            ),
        )
    }
}

@JsonNaming(value = SnakeCaseStrategy::class)
data class Response(
    val id: String,
    val nickname: String,
    val name: String?,
    val email: String,
    val gender: String?,
    val age: String?,
    val birthday: String?,
    val profileImage: String?,
    val birthyear: String?,
    val mobile: String?,
)
