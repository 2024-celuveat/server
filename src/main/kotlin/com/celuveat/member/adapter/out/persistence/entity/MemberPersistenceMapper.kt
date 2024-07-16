package com.celuveat.member.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier

@Mapper
class MemberPersistenceMapper {

    fun toEntity(member: Member): MemberJpaEntity {
        return MemberJpaEntity(
            id = member.id,
            nickname = member.nickname,
            profileImageUrl = member.profileImageUrl,
            serverType = member.socialIdentifier.serverType,
            socialId = member.socialIdentifier.socialId,
        )
    }

    fun toDomain(memberJpaEntity: MemberJpaEntity): Member {
        return Member(
            id = memberJpaEntity.id,
            nickname = memberJpaEntity.nickname,
            profileImageUrl = memberJpaEntity.profileImageUrl,
            socialIdentifier = SocialIdentifier(
                serverType = memberJpaEntity.serverType,
                socialId = memberJpaEntity.socialId,
            )
        )
    }
}
