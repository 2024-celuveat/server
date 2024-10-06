package com.celuveat.member.domain

import com.celuveat.review.exception.NoAuthorityReviewException

class Member(
    val id: Long = 0,
    var nickname: String,
    var profileImageUrl: String?,
    val email: String,
    val socialIdentifier: SocialIdentifier,
) {
    fun validateOwner(member: Member) {
        if (this != member) {
            throw NoAuthorityReviewException
        }
    }

    fun updateProfile(
        nickname: String,
        profileImageUrl: String,
    ) {
        this.nickname = nickname
        this.profileImageUrl = profileImageUrl
    }

    fun updateRefreshToken(refreshToken: String) {
        socialIdentifier.refreshToken = refreshToken
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
