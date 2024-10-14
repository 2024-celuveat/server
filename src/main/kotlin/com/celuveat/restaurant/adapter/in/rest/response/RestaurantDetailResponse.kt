package com.celuveat.restaurant.adapter.`in`.rest.response

import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import com.celuveat.restaurant.application.port.`in`.result.RestaurantDetailResult
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
        description = "관심 등록 여부",
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
    @Schema(
        description = "리뷰 수",
    )
    val reviewCount: Int,
    @Schema(
        description = "좋아요 수",
    )
    val likeCount: Int,
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
                reviewCount = result.reviewCount,
                likeCount = result.likeCount,
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

data class RestaurantDetailResponse(
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
        description = "영업 시간",
        example = """
            월 10:00 ~ 20:00
            화 10:00 ~ 20:00
            수 15:00 ~ 20:00
            목 10:00 ~ 20:00
            금 10:00 ~ 20:00
            토 10:00 ~ 18:00
            일 휴무
        """,
        nullable = true,
    )
    val businessHours: String?,
    @Schema(
        description = "소개",
        example = "맛있는 한식집",
        nullable = true,
    )
    val introduction: String?,
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
        description = "이미지 목록",
    )
    val images: List<RestaurantImageResponse>,
    @Schema(
        description = "관심 등록 여부",
        example = "true",
    )
    val liked: Boolean,
    @Schema(
        description = "방문한 연예인 목록",
    )
    val visitedCelebrities: List<SimpleCelebrityResponse>,
) {
    companion object {
        fun from(result: RestaurantDetailResult): RestaurantDetailResponse {
            return RestaurantDetailResponse(
                id = result.id,
                name = result.name,
                category = result.category,
                roadAddress = result.roadAddress,
                phoneNumber = result.phoneNumber,
                businessHours = result.businessHours,
                introduction = result.introduction,
                naverMapUrl = result.naverMapUrl,
                latitude = result.latitude,
                longitude = result.longitude,
                images = result.images.map { RestaurantImageResponse.from(it) },
                liked = result.liked,
                visitedCelebrities = result.visitedCelebrities.map { SimpleCelebrityResponse.from(it) },
            )
        }
    }
}
