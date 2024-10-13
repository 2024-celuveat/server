package com.celuveat.celeb.application.port.`in`.query

import com.celuveat.common.utils.geometry.SquarePolygon

data class ReadCelebritiesInRestaurantConditionQuery(
    val category: String?,
    val region: String?,
    val searchArea: SquarePolygon?,
)
