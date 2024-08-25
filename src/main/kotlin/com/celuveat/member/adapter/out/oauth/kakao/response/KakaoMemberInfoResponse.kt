package com.celuveat.member.adapter.out.oauth.kakao.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class KakaoMemberInfoResponse(
    private val id: String,
    private val kakaoAccount: KakaoAccount,
) {
    fun toMember(): Member {
        return Member(
            nickname = kakaoAccount.profile.nickname,
            profileImageUrl = kakaoAccount.profile.profileImageUrl,
            email = kakaoAccount.email,
            socialIdentifier = SocialIdentifier(
                serverType = SocialLoginType.KAKAO,
                socialId = id,
            ),
        )
    }
}

data class KakaoAccount(
    val profile: Profile,
    val email: String,
)

@JsonNaming(value = SnakeCaseStrategy::class)
data class Profile(
    val nickname: String,
    val profileImageUrl: String,
)
