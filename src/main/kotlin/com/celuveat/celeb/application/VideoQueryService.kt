package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.ReadVideosByRestaurantUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityWithSubscriberCountResult
import com.celuveat.celeb.application.port.`in`.result.VideoWithCelebrityResult
import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.application.port.out.ReadVideoPort
import org.springframework.stereotype.Service

@Service
class VideoQueryService(
    private val readVideoPort: ReadVideoPort,
    private val readCelebritiesPort: ReadCelebritiesPort,
) : ReadVideosByRestaurantUseCase {
    override fun readVideosByRestaurant(restaurantId: Long): List<VideoWithCelebrityResult> {
        val videos = readVideoPort.readVideosInRestaurant(restaurantId)
        val youtubeContentIds = videos.map { it.youtubeContent.id }
        val celebrities = readCelebritiesPort.readByYoutubeContentIds(youtubeContentIds)
        return videos.map { video ->
            VideoWithCelebrityResult(
                id = video.id,
                videoUrl = video.videoUrl,
                celebrities = celebrities.map { CelebrityWithSubscriberCountResult.from(it) },
            )
        }
    }
}
