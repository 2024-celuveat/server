package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "restaurant")
@Entity
class RestaurantJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val category: String,
    val roadAddress: String,
    val phoneNumber: String?,
    val naverMapUrl: String,
    val latitude: Double,
    val longitude: Double,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
