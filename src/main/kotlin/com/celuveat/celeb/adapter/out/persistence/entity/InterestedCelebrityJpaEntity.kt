package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Table(
    name = "interested_celebrity",
    uniqueConstraints = [UniqueConstraint(columnNames = ["member_id", "celebrity_id"])],
)
@Entity
class InterestedCelebrityJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: MemberJpaEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val celebrity: CelebrityJpaEntity,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
