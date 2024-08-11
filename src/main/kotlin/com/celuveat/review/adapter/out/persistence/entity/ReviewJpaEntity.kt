package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.ConstraintMode.NO_CONSTRAINT
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime

@Entity
class ReviewJpaEntity(
    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", foreignKey = ForeignKey(NO_CONSTRAINT))
    val restaurant: RestaurantJpaEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = ForeignKey(NO_CONSTRAINT))
    val writer: MemberJpaEntity,
    var content: String,
    // 별점
    var star: Int,
    // 조회수
    var views: Long = 0,
    // '도움돼요' 수.
    var helps: Long = 0,
    images: List<String> = emptyList(),
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : RootEntity<Long>() {
    @OneToMany(mappedBy = "review", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: List<ReviewImageJpaEntity>

    init {
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.images = images.map { ReviewImageJpaEntity(review = this, imageUrl = it) }
    }

    override fun id(): Long {
        return this.id
    }
}
