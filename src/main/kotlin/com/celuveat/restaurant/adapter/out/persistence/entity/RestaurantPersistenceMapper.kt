package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.restaurant.domain.RestaurantImage

@Mapper
class RestaurantPersistenceMapper {
    fun toDomain(
        restaurantJpaEntity: RestaurantJpaEntity,
        restaurantImageJpaEntities: List<RestaurantImageJpaEntity>,
    ): Restaurant {
        return Restaurant(
            id = restaurantJpaEntity.id,
            name = restaurantJpaEntity.name,
            category = restaurantJpaEntity.category,
            roadAddress = restaurantJpaEntity.roadAddress,
            phoneNumber = restaurantJpaEntity.phoneNumber,
            naverMapUrl = restaurantJpaEntity.naverMapUrl,
            latitude = restaurantJpaEntity.latitude,
            longitude = restaurantJpaEntity.longitude,
            images = restaurantImageJpaEntities.map { imageJpaEntity ->
                RestaurantImage(
                    id = imageJpaEntity.id,
                    name = imageJpaEntity.name,
                    author = imageJpaEntity.author,
                    url = imageJpaEntity.url,
                    isThumbnail = imageJpaEntity.isThumbnail,
                )
            },
        )
    }
}
