package com.celuveat.restaurant.application.port.`in`.result

import com.celuveat.celeb.application.port.`in`.result.SimpleCelebrityResult
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.restaurant.domain.RestaurantImage

data class RestaurantPreviewResult(
    val id: Long = 0,
    val name: String,
    val category: String,
    val roadAddress: String,
    val phoneNumber: String?,
    val naverMapUrl: String,
    val latitude: Double,
    val longitude: Double,
    val liked: Boolean,
    val visitedCelebrities: List<SimpleCelebrityResult>,
    val images: List<RestaurantImageResult>,
) {
    companion object {
        fun of(
            restaurant: Restaurant,
            liked: Boolean,
            visitedCelebrities: List<Celebrity> = emptyList(),
        ): RestaurantPreviewResult {
            return RestaurantPreviewResult(
                id = restaurant.id,
                name = restaurant.name,
                category = restaurant.category,
                roadAddress = restaurant.roadAddress,
                phoneNumber = restaurant.phoneNumber,
                naverMapUrl = restaurant.naverMapUrl,
                latitude = restaurant.latitude,
                longitude = restaurant.longitude,
                liked = liked,
                visitedCelebrities = visitedCelebrities.map { SimpleCelebrityResult.from(it) },
                images = restaurant.images.map { RestaurantImageResult.from(it) },
            )
        }
    }
}

data class RestaurantImageResult(
    val name: String,
    val author: String,
    val url: String,
    val isThumbnail: Boolean,
) {
    companion object {
        fun from(restaurantImage: RestaurantImage): RestaurantImageResult {
            return RestaurantImageResult(
                name = restaurantImage.name,
                author = restaurantImage.author,
                url = restaurantImage.url,
                isThumbnail = restaurantImage.isThumbnail,
            )
        }
    }
}

data class RestaurantDetailResult(
    val id: Long = 0,
    val name: String,
    val category: String,
    val roadAddress: String,
    val phoneNumber: String?,
    val businessHours: String?,
    val introduction: String?,
    val naverMapUrl: String,
    val latitude: Double,
    val longitude: Double,
    val images: List<RestaurantImageResult>,
    val liked: Boolean,
    val visitedCelebrities: List<SimpleCelebrityResult>,
) {
    companion object {
        fun of(
            restaurant: Restaurant,
            liked: Boolean,
            visitedCelebrities: List<Celebrity> = emptyList(),
        ): RestaurantDetailResult {
            return RestaurantDetailResult(
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
                images = restaurant.images.map { RestaurantImageResult.from(it) },
                liked = liked,
                visitedCelebrities = visitedCelebrities.map { SimpleCelebrityResult.from(it) },
            )
        }
    }
}
