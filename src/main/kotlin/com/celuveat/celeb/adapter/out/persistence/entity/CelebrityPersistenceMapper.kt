package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.ChannelId
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.annotation.Mapper

@Mapper
class CelebrityPersistenceMapper {
    fun toDomain(
        celebrity: CelebrityJpaEntity,
        youtubeContents: List<YoutubeContentJpaEntity>,
    ): Celebrity {
        return Celebrity(
            id = celebrity.id,
            name = celebrity.name,
            profileImageUrl = celebrity.profileImageUrl,
            introduction = celebrity.introduction,
            youtubeContents = youtubeContents.map {
                YoutubeContent(
                    id = it.id,
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

    fun toDomainWithoutYoutubeContent(entity: CelebrityJpaEntity): Celebrity {
        return Celebrity(
            id = entity.id,
            name = entity.name,
            profileImageUrl = entity.profileImageUrl,
            introduction = entity.introduction,
            youtubeContents = emptyList(),
        )
    }
}
