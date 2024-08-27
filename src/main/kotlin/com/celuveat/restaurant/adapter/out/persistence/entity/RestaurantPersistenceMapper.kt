package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.restaurant.domain.RestaurantImage

@Mapper
class RestaurantPersistenceMapper {
    fun toDomain(
        restaurant: RestaurantJpaEntity,
        restaurantImages: List<RestaurantImageJpaEntity>,
    ): Restaurant {
        return Restaurant(
            id = restaurant.id,
            name = restaurant.name,
            category = restaurant.category,
            roadAddress = restaurant.roadAddress,
            phoneNumber = restaurant.phoneNumber,
            businessHours = restaurant.businessHours,
            introduction = restaurant.introduction,
            naverMapUrl = restaurant.naverMapUrl,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            images = restaurantImages.map { imageJpaEntity ->
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

    fun toDomainWithoutImage(restaurant: RestaurantJpaEntity): Restaurant {
        return Restaurant(
            id = restaurant.id,
            name = restaurant.name,
            category = restaurant.category,
            roadAddress = restaurant.roadAddress,
            phoneNumber = restaurant.phoneNumber,
            businessHours = restaurant.businessHours,
            introduction = restaurant.introduction,
            naverMapUrl = restaurant.naverMapUrl,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            images = emptyList(),
        )
    }

    fun toEntity(restaurant: Restaurant): RestaurantJpaEntity {
        return RestaurantJpaEntity(
            id = restaurant.id,
            name = restaurant.name,
            category = restaurant.category,
            roadAddress = restaurant.roadAddress,
            phoneNumber = restaurant.phoneNumber,
            businessHours = restaurant.businessHours,
            introduction = restaurant.introduction,
            naverMapUrl = restaurant.naverMapUrl,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
        )
    }
}
