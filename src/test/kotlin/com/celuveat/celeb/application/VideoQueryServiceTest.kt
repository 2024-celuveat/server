package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.application.port.out.ReadVideoPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.Video
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll

class VideoQueryServiceTest : BehaviorSpec({
    val readVideoPort: ReadVideoPort = mockk()
    val readCelebritiesPort: ReadCelebritiesPort = mockk()

    val videoQueryService = VideoQueryService(
        readVideoPort,
        readCelebritiesPort,
    )

    Given("음식점이 나온 영상 조회 시") {
        val restaurantId = 1L
        val youtubeContents = sut.giveMeBuilder<YoutubeContent>()
            .setInner(channelIdSpec)
            .sampleList(2)
        val videos = youtubeContents.map { generateVideoWithYoutubeContent(it) }
        val youtubeContentIds = youtubeContents.map { it.id }
        val celebrities = sut.giveMeBuilder<Celebrity>()
            .set(Celebrity::youtubeContents, youtubeContents)
            .sampleList(2)

        When("음식점이 나온 영상을 조회하면") {
            every { readVideoPort.readVideosInRestaurant(restaurantId) } returns videos
            every { readCelebritiesPort.readByYoutubeContentIds(youtubeContentIds) } returns celebrities

            val result = videoQueryService.readVideosByRestaurant(restaurantId)
            Then("영상이 조회된다") {
                result.size shouldBe 2
            }
        }
    }
}) {
    override suspend fun afterEach(
        testCase: TestCase,
        result: TestResult,
    ) {
        clearAllMocks()
        unmockkAll()
    }
}

private fun generateVideoWithYoutubeContent(youtubeContent: YoutubeContent) =
    sut.giveMeBuilder<Video>().set(Video::youtubeContent, youtubeContent).sample()
