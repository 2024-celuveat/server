package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.query.ReadCelebritiesInRestaurantConditionQuery
import com.celuveat.celeb.application.port.`in`.result.SimpleCelebrityResult

interface ReadCelebritiesInRestaurantConditionUseCase {

    fun readCelebrities(query: ReadCelebritiesInRestaurantConditionQuery): List<SimpleCelebrityResult>
}
