package com.celuveat.member.domain

class Member(
    val id: Long = 0,
    var nickname: String,
    var profileImageUrl: String?,
    val email: String,
    val socialIdentifier: SocialIdentifier,
) {
    fun updateProfile(nickname: String, profileImageUrl: String) {
        this.nickname = nickname
        this.profileImageUrl = profileImageUrl
    }
}
