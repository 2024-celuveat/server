package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeChannel

data class CelebrityResult(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,
    val youtubeChannelResults: List<YoutubeChannelResult>,
) {
    companion object {
        fun from(celebrity: Celebrity): CelebrityResult {
            return CelebrityResult(
                id = celebrity.id,
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
                introduction = celebrity.introduction,
                youtubeChannelResults = celebrity.youtubeChannels.map { YoutubeChannelResult.from(it) },
            )
        }
    }
}

data class YoutubeChannelResult(
    val id: Long,
    val channelId: String,
    val channelUrl: String,
    val channelName: String,
    val contentsName: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
) {
    companion object {
        fun from(youtubeChannel: YoutubeChannel): YoutubeChannelResult {
            return YoutubeChannelResult(
                id = youtubeChannel.id,
                channelId = youtubeChannel.channelId.value,
                channelUrl = youtubeChannel.channelUrl,
                channelName = youtubeChannel.channelName,
                contentsName = youtubeChannel.contentsName,
                restaurantCount = youtubeChannel.restaurantCount,
                subscriberCount = youtubeChannel.subscriberCount,
            )
        }
    }
}
