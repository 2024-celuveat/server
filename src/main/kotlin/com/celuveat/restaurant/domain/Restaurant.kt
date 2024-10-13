package com.celuveat.restaurant.domain

data class Restaurant(
    val id: Long = 0,
    val name: String,
    val category: String,
    val roadAddress: String,
    val phoneNumber: String?,
    val businessHours: String?,
    val introduction: String?,
    val naverMapUrl: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<RestaurantImage>,
    var reviewCount: Int = 0,
    var likeCount: Int = 0,
) {
    fun increaseReviewCount() {
        reviewCount += 1;
    }

    fun decreaseReviewCount() {
        reviewCount -= 1;
    }

    fun increaseLikeCount() {
        likeCount += 1;
    }

    fun decreaseLikeCount() {
        likeCount -= 1;
    }
}
