package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.member.domain.OAuthServerType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {

    fun findMemberByOAuthIdAndServerType(oAuthId: String, oAuthServerType: OAuthServerType): MemberJpaEntity?
}
