package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@PersistenceAdapterTest
class VideoPersistenceAdapterTest(
    private val videoPersistenceAdapter: VideoPersistenceAdapter,
    private val videoJpaRepository: VideoJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
) : FunSpec({
    test("음식점이 나온 영상을 조회 한다.") {
        // given
        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelAId")
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelBId")
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB))
        val savedVideos = videoJpaRepository.saveAll(
            listOf(
                generateVideoWithYoutubeContent(savedContents[0]),
                generateVideoWithYoutubeContent(savedContents[1]),
            ),
        )
        val restaurant = restaurantJpaRepository.save(sut.giveMeBuilder<RestaurantJpaEntity>().sample())
        restaurantInVideoJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[0])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurant)
                    .sample(),
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[1])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurant)
                    .sample(),
            ),
        ) // [음식점] -> [영상A, 영상B] 에 출연함

        // when
        val videos = videoPersistenceAdapter.readVideosInRestaurant(restaurant.id)

        // then
        videos.size shouldBe 2
        videos.forAll { it.youtubeContent shouldNotBe null }
    }
})

private fun generateVideoWithYoutubeContent(youtubeContent: YoutubeContentJpaEntity) =
    sut.giveMeBuilder<VideoJpaEntity>().set(VideoJpaEntity::youtubeContent, youtubeContent).sample()
