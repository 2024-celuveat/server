package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.domain.OAuthServerType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class MemberJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nickname: String,
    val profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    val serverType: OAuthServerType,
    val oAuthId: String,
) : RootEntity<Long>() {

    override fun id(): Long {
        return this.id
    }
}
