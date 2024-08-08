package com.celuveat.restaurant.domain

import com.celuveat.member.domain.Member

data class InterestedRestaurant(
    val member: Member,
    val restaurant: Restaurant,
)
