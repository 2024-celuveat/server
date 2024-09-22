package com.celuveat.region.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "region")
@Entity
class RegionJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val name: String,
    val latitude: Double,
    val longitude: Double,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
