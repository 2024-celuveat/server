package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class CelebrityRestaurantJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val celebrity: CelebrityJpaEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val restaurant: RestaurantJpaEntity
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
