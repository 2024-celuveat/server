package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.ChannelId
import com.celuveat.celeb.domain.YoutubeChannel
import com.celuveat.common.annotation.Mapper

@Mapper
class CelebrityPersistenceMapper {
    fun toDomain(
        celebrityJpaEntity: CelebrityJpaEntity,
        youtubeChannelJpaEntities: List<YoutubeChannelJpaEntity>,
    ): Celebrity {
        return Celebrity(
            id = celebrityJpaEntity.id,
            name = celebrityJpaEntity.name,
            profileImageUrl = celebrityJpaEntity.profileImageUrl,
            introduction = celebrityJpaEntity.introduction,
            youtubeChannels = youtubeChannelJpaEntities.map { youtubeChannelJpaEntity ->
                YoutubeChannel(
                    channelId = ChannelId(youtubeChannelJpaEntity.channelId),
                    channelUrl = youtubeChannelJpaEntity.channelUrl,
                    channelName = youtubeChannelJpaEntity.channelName,
                    contentsName = youtubeChannelJpaEntity.contentsName,
                    contentsIntroduction = youtubeChannelJpaEntity.contentsIntroduction,
                    restaurantCount = youtubeChannelJpaEntity.restaurantCount,
                    subscriberCount = youtubeChannelJpaEntity.subscriberCount,
                )
            },
        )
    }
}
