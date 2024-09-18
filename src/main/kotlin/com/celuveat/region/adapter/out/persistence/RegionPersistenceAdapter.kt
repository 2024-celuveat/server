package com.celuveat.region.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.region.adapter.out.persistence.entity.RegionJpaRepository
import com.celuveat.region.application.port.out.ReadRegionPort

@Adapter
class RegionPersistenceAdapter(
    private val regionJpaRepository: RegionJpaRepository
) : ReadRegionPort {
}
