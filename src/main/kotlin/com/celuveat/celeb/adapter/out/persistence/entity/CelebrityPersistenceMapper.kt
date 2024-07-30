package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeChannel
import com.celuveat.common.annotation.Mapper

@Mapper
class CelebrityPersistenceMapper {
    fun toDomain(celebrityJpaEntity: CelebrityJpaEntity): Celebrity {
        return Celebrity(
            id = celebrityJpaEntity.id,
            name = celebrityJpaEntity.name,
            profileImageUrl = celebrityJpaEntity.profileImageUrl,
            introduction = celebrityJpaEntity.introduction,
            youtubeChannels = celebrityJpaEntity.youtubeChannels.map { youtubeChannelJpaEntity ->
                YoutubeChannel(
                    channelId = youtubeChannelJpaEntity.channelId,
                    channelUrl = youtubeChannelJpaEntity.channelUrl,
                    contentsName = youtubeChannelJpaEntity.contentsName,
                    contentsIntroduction = youtubeChannelJpaEntity.contentsIntroduction,
                    subscriberCount = youtubeChannelJpaEntity.subscriberCount,
                )
            },
        )
    }
}
