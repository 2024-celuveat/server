package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.member.exception.NotFoundMemberException
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {
    fun findMemberBySocialIdAndServerType(
        socialId: String,
        oAuthServerType: SocialLoginType,
    ): MemberJpaEntity?

    override fun getById(id: Long): MemberJpaEntity {
        return findByIdOrThrow(id) { NotFoundMemberException }
    }
}
