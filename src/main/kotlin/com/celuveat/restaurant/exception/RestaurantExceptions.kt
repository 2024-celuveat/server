package com.celuveat.restaurant.exception

import com.celuveat.common.exception.CeluveatException
import com.celuveat.member.exception.NotFoundMemberException
import org.springframework.http.HttpStatus

sealed class RestaurantExceptions(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object NotFoundRestaurantException : RestaurantExceptions(HttpStatus.NOT_FOUND, "존재 하지 않는 식당입니다.") {
    private fun readResolve(): Any = NotFoundMemberException
}

data object AlreadyInterestedRestaurantException :
    RestaurantExceptions(HttpStatus.BAD_REQUEST, "이미 관심 식당으로 등록된 식당입니다.") {
    private fun readResolve(): Any = NotFoundMemberException
}

data object NotFoundInterestedRestaurantException : RestaurantExceptions(HttpStatus.NOT_FOUND, "관심 식당을 찾을 수 없습니다.") {
    private fun readResolve(): Any = NotFoundMemberException
}
