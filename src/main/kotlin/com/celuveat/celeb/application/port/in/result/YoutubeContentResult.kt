package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.YoutubeContent

data class YoutubeContentResult(
    val id: Long,
    val channelId: String,
    val channelUrl: String,
    val channelName: String,
    val contentsName: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
) {
    companion object {
        fun from(youtubeContent: YoutubeContent): YoutubeContentResult {
            return YoutubeContentResult(
                id = youtubeContent.id,
                channelId = youtubeContent.channelId.value,
                channelUrl = youtubeContent.channelUrl,
                channelName = youtubeContent.channelName,
                contentsName = youtubeContent.contentsName,
                restaurantCount = youtubeContent.restaurantCount,
                subscriberCount = youtubeContent.subscriberCount,
            )
        }
    }
}
