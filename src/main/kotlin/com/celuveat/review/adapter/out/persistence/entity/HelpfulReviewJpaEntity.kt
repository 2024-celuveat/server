package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
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
import jakarta.persistence.UniqueConstraint

@Table(
    name = "helpful_review",
    uniqueConstraints = [UniqueConstraint(columnNames = ["review_id", "member_id"])],
)
@Entity
class HelpfulReviewJpaEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", foreignKey = ForeignKey(NO_CONSTRAINT))
    val review: ReviewJpaEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(NO_CONSTRAINT))
    val member: MemberJpaEntity,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
