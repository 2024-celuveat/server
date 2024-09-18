package com.celuveat.region.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface RegionJpaRepository : JpaRepository<RegionJpaEntity, Long> {
}
