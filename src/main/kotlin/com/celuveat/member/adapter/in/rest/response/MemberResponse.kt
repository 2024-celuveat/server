package com.celuveat.member.adapter.`in`.rest.response

import com.celuveat.member.application.port.`in`.result.MemberResult
import com.celuveat.member.domain.SocialLoginType
import io.swagger.v3.oas.annotations.media.Schema

data class MemberResponse(

    @Schema(
        description = "회원 ID",
        example = "1",
    )
    val id: Long = 0,
    @Schema(
        description = "닉네임",
        example = "닉네임",
    )
    val nickname: String,
    @Schema(
        description = "프로필 이미지 URL",
        example = "https://example.com/profile.jpg",
    )
    val profileImageUrl: String?,
    @Schema(
        description = "이메일",
        example = "email@celuveat.com"
    )
    val email: String,
    @Schema(
        description = "소셜 로그인 타입",
        example = "KAKAO",
    )
    val serverType: SocialLoginType,
    @Schema(
        description = "소셜 ID",
        example = "123",
    )
    val socialId: String,
) {
    companion object {
        fun from(result: MemberResult): MemberResponse {
            return MemberResponse(
                id = result.id,
                nickname = result.nickname,
                profileImageUrl = result.profileImageUrl,
                email = result.email,
                serverType = result.serverType,
                socialId = result.socialId,
            )
        }
    }
}
