package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.restaurant.domain.InterestedRestaurant

@Mapper
class InterestedRestaurantPersistenceMapper(
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
    private val memberPersistenceMapper: MemberPersistenceMapper,
) {
    fun toDomain(
        interestedRestaurant: InterestedRestaurantJpaEntity,
        restaurantImages: List<RestaurantImageJpaEntity>,
    ): InterestedRestaurant {
        return InterestedRestaurant(
            member = memberPersistenceMapper.toDomain(interestedRestaurant.member),
            restaurant = restaurantPersistenceMapper.toDomain(interestedRestaurant.restaurant, restaurantImages),
        )
    }

    fun toDomain(interestedRestaurant: InterestedRestaurantJpaEntity): InterestedRestaurant {
        return InterestedRestaurant(
            member = memberPersistenceMapper.toDomain(interestedRestaurant.member),
            restaurant = restaurantPersistenceMapper.toDomainWithoutImage(interestedRestaurant.restaurant),
        )
    }
}
