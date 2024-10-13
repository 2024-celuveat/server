package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.domain.SocialLoginType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "member")
@Entity
class MemberJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nickname: String,
    val profileImageUrl: String?,
    val email: String,
    @Enumerated(EnumType.STRING)
    val serverType: SocialLoginType,
    val socialId: String,
    var refreshToken: String,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
