package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.ChannelId
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.annotation.Mapper

@Mapper
class CelebrityPersistenceMapper {
    fun toDomain(
        celebrityJpaEntity: CelebrityJpaEntity,
        youtubeContentsJpaEntities: List<YoutubeContentJpaEntity>,
    ): Celebrity {
        return Celebrity(
            id = celebrityJpaEntity.id,
            name = celebrityJpaEntity.name,
            profileImageUrl = celebrityJpaEntity.profileImageUrl,
            introduction = celebrityJpaEntity.introduction,
            youtubeContents = youtubeContentsJpaEntities.map {
                YoutubeContent(
                    channelId = ChannelId(it.channelId),
                    channelUrl = it.channelUrl,
                    channelName = it.channelName,
                    contentsName = it.contentsName,
                    contentsIntroduction = it.contentsIntroduction,
                    restaurantCount = it.restaurantCount,
                    subscriberCount = it.subscriberCount,
                )
            },
        )
    }

    fun toDomainWithoutYoutubeContent(
        celebrityJpaEntity: CelebrityJpaEntity,
    ): Celebrity {
        return Celebrity(
            id = celebrityJpaEntity.id,
            name = celebrityJpaEntity.name,
            profileImageUrl = celebrityJpaEntity.profileImageUrl,
            introduction = celebrityJpaEntity.introduction,
            youtubeContents = emptyList(),
        )
    }
}
