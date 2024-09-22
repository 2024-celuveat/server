package com.celuveat.region.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.region.adapter.out.persistence.entity.RegionJpaRepository
import com.celuveat.region.adapter.out.persistence.entity.RegionPersistenceMapper
import com.celuveat.region.application.port.out.ReadRegionPort
import com.celuveat.region.domain.Region

@Adapter
class RegionPersistenceAdapter(
    private val regionJpaRepository: RegionJpaRepository,
    private val regionPersistenceMapper: RegionPersistenceMapper,
) : ReadRegionPort {
    override fun readByName(name: String): List<Region> {
        val regions = regionJpaRepository.readByNameContains(name)
        return regions.map { regionPersistenceMapper.toDomain(it) }
    }
}
