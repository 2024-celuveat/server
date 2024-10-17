package com.celuveat.region.application.port.`in`

import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult

interface ReadRepresentativeRegionsUseCase {
    fun readRepresentativeRegions(): List<RepresentativeRegionResult>
}
