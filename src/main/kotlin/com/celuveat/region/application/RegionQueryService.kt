package com.celuveat.region.application

import com.celuveat.region.application.port.`in`.ReadRepresentativeRegionsUseCase
import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult
import com.celuveat.region.application.port.out.ReadRegionPort
import org.springframework.stereotype.Service

@Service
class RegionQueryService(
    private val readRegionPort: ReadRegionPort,
) : ReadRepresentativeRegionsUseCase {
    override fun readRepresentativeRegions(): List<RepresentativeRegionResult> {
        return readRegionPort.readRepresentativeRegions()
    }
}
