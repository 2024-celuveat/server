package com.celuveat.restaurant.adapter.`in`.rest.request

import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn

data class ReadRestaurantsRequest(
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "지역",
        example = "성수",
        required = false,
    )
    val region: String?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "카테고리",
        example = "한식",
        required = false,
    )
    val category: String?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "검색 영역의 최소 경도",
        example = "127.0",
        required = false,
    )
    val lowLongitude: Double?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "검색 영역의 최대 경도",
        example = "128.0",
        required = false,
    )
    val highLongitude: Double?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "검색 영역의 최소 위도",
        example = "35.0",
        required = false,
    )
    val lowLatitude: Double?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "검색 영역의 최대 위도",
        example = "36.0",
        required = false,
    )
    val highLatitude: Double?,
    @Parameter(
        `in` = ParameterIn.QUERY,
        description = "셀럽 ID",
        example = "1",
        required = false,
    )
    val celebrityId: Long?,
) {
    fun toQuery(
        memberId: Long?,
        page: Int,
        size: Int,
    ): ReadRestaurantsQuery {
        return ReadRestaurantsQuery(
            memberId = memberId,
            region = region,
            category = category,
            searchArea = SquarePolygon.ofNullable(
                lowLongitude = lowLongitude,
                highLongitude = highLongitude,
                lowLatitude = lowLatitude,
                highLatitude = highLatitude,
            ),
            celebrityId = celebrityId,
            page = page,
            size = size,
        )
    }
}
