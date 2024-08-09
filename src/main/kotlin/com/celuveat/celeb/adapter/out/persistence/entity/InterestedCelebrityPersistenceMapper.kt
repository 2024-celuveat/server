package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.InterestedCelebrity
import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper

@Mapper
class InterestedCelebrityPersistenceMapper(
    private val memberPersistenceMapper: MemberPersistenceMapper,
    private val celebrityPersistenceMapper: CelebrityPersistenceMapper,
) {
    fun toDomain(
        interestedCelebrity: InterestedCelebrityJpaEntity,
        youtubeContents: List<YoutubeContentJpaEntity>,
    ): InterestedCelebrity {
        return InterestedCelebrity(
            member = memberPersistenceMapper.toDomain(interestedCelebrity.member),
            celebrity = celebrityPersistenceMapper.toDomain(interestedCelebrity.celebrity, youtubeContents),
        )
    }

    fun toEntity(
        celebrity: CelebrityJpaEntity,
        member: MemberJpaEntity,
    ): InterestedCelebrityJpaEntity {
        return InterestedCelebrityJpaEntity(
            celebrity = celebrity,
            member = member,
        )
    }
}
