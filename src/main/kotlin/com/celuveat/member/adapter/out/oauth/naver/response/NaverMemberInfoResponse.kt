package com.celuveat.member.adapter.out.oauth.naver.response

import com.celuveat.member.adapter.out.oauth.response.SocialLoginInfoResponse
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class NaverMemberInfoResponse(
    private val resultcode: String,
    private val message: String,
    private val response: Response
) {

    fun toSocialLoginInfoResponse(): SocialLoginInfoResponse {
        return SocialLoginInfoResponse(
            id = response.id,
            nickname = response.nickname,
            profileImage = response.profileImage,
            serverType = SocialLoginType.NAVER,
        )
    }
}

@JsonNaming(value = SnakeCaseStrategy::class)
data class Response(
    val id: String,
    val nickname: String,
    val name: String,
    val email: String,
    val gender: String,
    val age: String,
    val birthday: String,
    val profileImage: String,
    val birthyear: String,
    val mobile: String
)
