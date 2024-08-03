package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.YoutubeChannel

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
