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
            email = member.email,
            serverType = member.socialIdentifier.serverType,
            socialId = member.socialIdentifier.socialId,
            refreshToken = member.socialIdentifier.refreshToken,
        )
    }

    fun toDomain(entity: MemberJpaEntity): Member {
        return Member(
            id = entity.id,
            nickname = entity.nickname,
            profileImageUrl = entity.profileImageUrl,
            email = entity.email,
            socialIdentifier = SocialIdentifier(
                serverType = entity.serverType,
                socialId = entity.socialId,
                refreshToken = entity.refreshToken,
            ),
        )
    }
}
