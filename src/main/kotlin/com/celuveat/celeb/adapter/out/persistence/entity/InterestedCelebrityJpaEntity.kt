package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class InterestedCelebrityJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne @JoinColumn(name = "member_id")
    val member: MemberJpaEntity,
    @ManyToOne @JoinColumn(name = "celebrity_id")
    val celebrity: CelebrityJpaEntity,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
