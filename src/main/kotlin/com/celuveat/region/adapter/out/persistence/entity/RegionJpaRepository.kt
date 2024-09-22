package com.celuveat.region.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RegionJpaRepository : JpaRepository<RegionJpaEntity, Long> {
    @Query(value = "SELECT r FROM RegionJpaEntity r WHERE r.name LIKE %:name%")
    fun readByNameContains(name: String): List<RegionJpaEntity>
}
