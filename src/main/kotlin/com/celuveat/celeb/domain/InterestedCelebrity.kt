package com.celuveat.celeb.domain

import com.celuveat.member.domain.Member

data class InterestedCelebrity(
    val member: Member,
    val celebrity: Celebrity,
)
