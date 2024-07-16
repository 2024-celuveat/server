package com.celuveat.member.adapter.out.oauth.kakao.response

import com.celuveat.member.adapter.out.oauth.response.SocialLoginInfoResponse
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class KakaoMemberInfoResponse(
    private val id: String,
    private val kakaoAccount: KakaoAccount,
) {

    fun toSocialLoginInfoResponse(): SocialLoginInfoResponse {
        return SocialLoginInfoResponse(
            id = id,
            nickname = kakaoAccount.profile.nickname,
            profileImage = kakaoAccount.profile.profileImage,
            serverType = SocialLoginType.KAKAO,
        )
    }
}

data class KakaoAccount(
    val profile: Profile,
)

@JsonNaming(value = SnakeCaseStrategy::class)
data class Profile(
    val nickname: String,
    val profileImage: String,
)
