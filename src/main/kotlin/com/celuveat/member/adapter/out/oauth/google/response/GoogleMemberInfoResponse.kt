package com.celuveat.member.adapter.out.oauth.google.response

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.member.domain.SocialLoginType
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class GoogleMemberInfoResponse(
    private val id: String,
    private val name: String,
    private val verifiedEmail: Boolean,
    private val givenName: String,
    private val picture: String,
    private val email: String,
) {
    fun toMember(refreshToken: String): Member {
        return Member(
            nickname = name,
            profileImageUrl = picture,
            email = email,
            socialIdentifier = SocialIdentifier(
                serverType = SocialLoginType.GOOGLE,
                socialId = id,
                refreshToken = refreshToken,
            ),
        )
    }
}
