package com.celuveat.restaurant.adapter.`in`.rest.response

import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import com.celuveat.restaurant.application.port.`in`.result.RestaurantImageResult
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import io.swagger.v3.oas.annotations.media.Schema

data class RestaurantPreviewResponse(
    @Schema(
        description = "식당 ID",
        example = "1",
    )
    val id: Long = 0,
    @Schema(
        description = "식당 이름",
        example = "맛집",
    )
    val name: String,
    @Schema(
        description = "카테고리",
        example = "한식",
    )
    val category: String,
    @Schema(
        description = "도로명 주소",
        example = "서울특별시 강남구 역삼동 123-456",
    )
    val roadAddress: String,
    @Schema(
        description = "전화번호",
        example = "02-1234-5678",
        nullable = true,
    )
    val phoneNumber: String?,
    @Schema(
        description = "네이버 지도 URL",
        example = "https://map.naver.com/v5/entry/place/12345678",
    )
    val naverMapUrl: String,
    @Schema(
        description = "위도",
        example = "37.123456",
    )
    val latitude: Double,
    @Schema(
        description = "경도",
        example = "127.123456",
    )
    val longitude: Double,
    @Schema(
        description = "좋아요 여부",
        example = "true",
    )
    val liked: Boolean,
    @Schema(
        description = "방문한 연예인 목록",
    )
    val visitedCelebrities: List<SimpleCelebrityResponse>,
    @Schema(
        description = "이미지 목록",
    )
    val images: List<RestaurantImageResponse>,
) {
    companion object {
        fun from(result: RestaurantPreviewResult): RestaurantPreviewResponse {
            return RestaurantPreviewResponse(
                id = result.id,
                name = result.name,
                category = result.category,
                roadAddress = result.roadAddress,
                phoneNumber = result.phoneNumber,
                naverMapUrl = result.naverMapUrl,
                latitude = result.latitude,
                longitude = result.longitude,
                liked = result.liked,
                visitedCelebrities = result.visitedCelebrities.map { SimpleCelebrityResponse.from(it) },
                images = result.images.map { RestaurantImageResponse.from(it) },
            )
        }
    }
}

data class RestaurantImageResponse(
    @Schema(
        description = "이미지 이름",
        example = "맛집",
    )
    val name: String,
    @Schema(
        description = "작성자",
        example = "홍길동",
    )
    val author: String,
    @Schema(
        description = "이미지 URL",
        example = "https://example.com/image.jpg",
    )
    val url: String,
    @Schema(
        description = "썸네일 여부",
        example = "true",
    )
    val isThumbnail: Boolean,
) {
    companion object {
        fun from(result: RestaurantImageResult): RestaurantImageResponse {
            return RestaurantImageResponse(
                name = result.name,
                author = result.author,
                url = result.url,
                isThumbnail = result.isThumbnail,
            )
        }
    }
}
