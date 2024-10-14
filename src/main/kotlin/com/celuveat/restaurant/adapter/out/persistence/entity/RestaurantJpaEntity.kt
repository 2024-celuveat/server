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
    val businessHours: String?,
    val introduction: String?,
    val naverMapUrl: String,
    val latitude: Double,
    val longitude: Double,
    val reviewCount: Int = 0,
    val likeCount: Int = 0,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
