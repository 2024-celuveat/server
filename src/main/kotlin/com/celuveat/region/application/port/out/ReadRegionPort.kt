package com.celuveat.region.application.port.out

import com.celuveat.region.domain.Region

interface ReadRegionPort {
    fun readByName(name: String): List<Region>
}
