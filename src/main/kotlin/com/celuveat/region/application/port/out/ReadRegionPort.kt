package com.celuveat.region.application.port.out

import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult
import com.celuveat.region.domain.Region

interface ReadRegionPort {
    fun readByName(name: String): List<Region>

    fun readRepresentativeRegions(): List<RepresentativeRegionResult>
}
