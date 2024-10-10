package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.ConstraintMode.NO_CONSTRAINT
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "review_image")
@Entity
class ReviewImageJpaEntity(
    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", foreignKey = ForeignKey(NO_CONSTRAINT))
    val review: ReviewJpaEntity,
    val imageUrl: String,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
