package com.celuveat.member.adapter.out.oauth.kakao.response

import com.celuveat.member.application.port.out.response.SocialLoginInfoResponse
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

// ref - https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
@JsonNaming(SnakeCaseStrategy::class)
class KakaoMemberInfoResponse(
    private val id: String,
    private val kakaoAccount: KakaoAccount,
) {

    fun toOAuthUserInfoResponse(): SocialLoginInfoResponse {
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

data class Profile(
    val nickname: String,
    val profileImage: String,
)
