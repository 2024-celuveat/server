package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.domain.ChannelId
import com.celuveat.celeb.domain.Video
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.annotation.Mapper

@Mapper
class VideoPersistenceMapper {
    fun toDomain(video: VideoJpaEntity): Video {
        return Video(
            id = video.id,
            videoUrl = video.videoUrl,
            uploadDate = video.uploadDate,
            youtubeContent = YoutubeContent(
                id = video.youtubeContent.id,
                channelId = ChannelId(video.youtubeContent.channelId),
                channelUrl = video.youtubeContent.channelUrl,
                channelName = video.youtubeContent.channelName,
                contentsName = video.youtubeContent.contentsName,
                contentsIntroduction = video.youtubeContent.contentsIntroduction,
                restaurantCount = video.youtubeContent.restaurantCount,
                subscriberCount = video.youtubeContent.subscriberCount,
            ),
        )
    }
}
