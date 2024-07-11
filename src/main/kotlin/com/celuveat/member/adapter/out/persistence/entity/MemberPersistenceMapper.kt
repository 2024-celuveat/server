package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.domain.Member

@Mapper
class MemberPersistenceMapper {

    fun toEntity(member: Member): MemberJpaEntity {
        return MemberJpaEntity(
            id = member.id,
            nickname = member.nickname,
            profileImageUrl = member.profileImageUrl,
            serverType = member.serverType,
            oAuthId = member.oAuthId,
        )
    }

    fun toDomain(memberJpaEntity: MemberJpaEntity): Member {
        return Member(
            id = memberJpaEntity.id,
            nickname = memberJpaEntity.nickname,
            profileImageUrl = memberJpaEntity.profileImageUrl,
            serverType = memberJpaEntity.serverType,
            oAuthId = memberJpaEntity.oAuthId,
        )
    }
}
