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
    private val givenName: String,
    private val picture: String,
    private val locale: String,
) {

    fun toMember(): Member {
        return Member(
            nickname = name,
            profileImageUrl = picture,
            socialIdentifier = SocialIdentifier(
                serverType = SocialLoginType.GOOGLE,
                socialId = id,
            )
        )
    }
}
