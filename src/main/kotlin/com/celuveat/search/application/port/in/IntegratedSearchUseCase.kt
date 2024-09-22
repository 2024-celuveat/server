package com.celuveat.search.application.port.`in`

import com.celuveat.search.application.port.`in`.query.IntegratedSearchQuery
import com.celuveat.search.application.port.`in`.result.IntegratedSearchResult

interface IntegratedSearchUseCase {
    fun searchByName(query: IntegratedSearchQuery): IntegratedSearchResult
}
