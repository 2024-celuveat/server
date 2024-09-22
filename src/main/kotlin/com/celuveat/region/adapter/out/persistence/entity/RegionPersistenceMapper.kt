package com.celuveat.region.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.region.domain.Region

@Mapper
class RegionPersistenceMapper {
    fun toDomain(region: RegionJpaEntity): Region {
        return Region(id = region.id, name = region.name, latitude = region.latitude, longitude = region.longitude)
    }

    fun toEntity(region: Region): RegionJpaEntity {
        return RegionJpaEntity(
            id = region.id,
            name = region.name,
            latitude = region.latitude,
            longitude = region.longitude,
        )
    }
}
