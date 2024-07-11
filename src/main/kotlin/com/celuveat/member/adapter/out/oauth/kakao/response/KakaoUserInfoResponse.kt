package com.celuveat.member.adapter.out.oauth.kakao.response

import com.celuveat.member.application.port.out.response.OAuthUserInfoResponse
import com.celuveat.member.domain.OAuthServerType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

// Remove comment after checking the reference below
// ref - https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
@JsonNaming(SnakeCaseStrategy::class)
class KakaoUserInfoResponse(
    private val id: String,
    private val kakaoAccount: KakaoAccount,
) {

    fun toOAuthUserInfoResponse(): OAuthUserInfoResponse {
        return OAuthUserInfoResponse(
            id = id,
            nickname = kakaoAccount.profile.nickname,
            profileImage = kakaoAccount.profile.profileImage,
            serverType = OAuthServerType.KAKAO,
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
