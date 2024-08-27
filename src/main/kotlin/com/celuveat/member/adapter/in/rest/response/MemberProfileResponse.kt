package com.celuveat.member.adapter.`in`.rest.response

import com.celuveat.member.application.port.`in`.result.MemberProfileResult
import com.celuveat.member.domain.SocialLoginType
import io.swagger.v3.oas.annotations.media.Schema

data class MemberProfileResponse(
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
    @Schema(
        description = "관심 등록 수",
        example = "1",
    )
    val interestedCount: Int,
    @Schema(
        description = "리뷰 수",
        example = "1",
    )
    val reviewCount: Int,
) {
    companion object {
        fun from(result: MemberProfileResult): MemberProfileResponse {
            return MemberProfileResponse(
                id = result.id,
                nickname = result.nickname,
                profileImageUrl = result.profileImageUrl,
                email = result.email,
                serverType = result.serverType,
                socialId = result.socialId,
                interestedCount = result.interestedCount,
                reviewCount = result.reviewCount,
            )
        }
    }
}
