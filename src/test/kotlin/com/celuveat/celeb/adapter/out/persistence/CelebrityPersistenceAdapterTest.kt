package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
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
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

@PersistenceAdapterTest
class CelebrityPersistenceAdapterTest(
    private val celebrityPersistenceAdapter: CelebrityPersistenceAdapter,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val videoJpaRepository: VideoJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
) : FunSpec({
    test("식당을 방문한 셀럽을 조회 한다.") {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelAId")
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelBId")
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB))
        celebrityYoutubeContentJpaRepository.saveAll(
            listOf(
                generateCelebrityYoutubeContent(celebrityA, savedContents[0]),
                generateCelebrityYoutubeContent(celebrityA, savedContents[1]),
                generateCelebrityYoutubeContent(celebrityB, savedContents[0]),
            ),
        ) // [셀럽A] -> [컨텐츠A, 컨텐츠B], [셀럽B] -> [컨텐츠A] 에 출연함

        val savedVideos = videoJpaRepository.saveAll(
            listOf(
                generateVideoWithYoutubeContent(savedContents[0]).sample(),
                generateVideoWithYoutubeContent(savedContents[1]).sample(),
                generateVideoWithYoutubeContent(savedContents[1]).sample(),
            ),
        ) // [영상1] -> [컨텐츠A], [영상2, 영상3] -> [컨텐츠B] 의 영상

        val restaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        restaurantInVideoJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[0])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[1])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[2])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[1])
                    .sample(),
            ),
        ) // [영상1, 영상2] -> [음식점1], [영상3] -> [음식점2] 에 방문함

        // when
        val visitedCelebritiesByRestaurants =
            celebrityPersistenceAdapter.readVisitedCelebritiesByRestaurants(restaurants.map { it.id })

        // then
        assertSoftly {
            visitedCelebritiesByRestaurants.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[0].id]!!.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[1].id]!!.size shouldBe 1
        }
    }

    test("구독자가 많은 컨텐츠의 셀럽순으로 조회 한다.") {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(3))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]
        val celebrityC = savedCelebrities[2]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::subscriberCount, 300)
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::subscriberCount, 200)
            .sample()
        val contentC = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::subscriberCount, 500)
            .sample()
        val contentD = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::subscriberCount, 400)
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB, contentC, contentD))
        celebrityYoutubeContentJpaRepository.saveAll(
            listOf(
                generateCelebrityYoutubeContent(celebrityA, savedContents[0]),
                generateCelebrityYoutubeContent(celebrityB, savedContents[1]),
                generateCelebrityYoutubeContent(celebrityC, savedContents[2]),
                generateCelebrityYoutubeContent(celebrityA, savedContents[3]),
            ),
        )

        // when
        val celebrities = celebrityPersistenceAdapter.readBestCelebrities()

        // then
        celebrities.size shouldBe 3
        celebrities.map { it.id } shouldContainExactly listOf(celebrityC.id, celebrityA.id, celebrityB.id)
    }

    test("셀럽을 조회 한다.") {
        // given
        val celebrity = celebrityJpaRepository.save(sut.giveMeOne<CelebrityJpaEntity>())
        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelAId")
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelBId")
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB))
        celebrityYoutubeContentJpaRepository.saveAll(
            listOf(
                generateCelebrityYoutubeContent(celebrity, savedContents[0]),
                generateCelebrityYoutubeContent(celebrity, savedContents[1]),
            ),
        )

        // when
        val findCelebrity = celebrityPersistenceAdapter.readById(celebrity.id)

        // then
        celebrity.id shouldBe findCelebrity.id
    }
})

private fun generateCelebrityYoutubeContent(
    celebrity: CelebrityJpaEntity?,
    savedContent: YoutubeContentJpaEntity,
): CelebrityYoutubeContentJpaEntity =
    sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
        .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrity)
        .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContent)
        .sample()

private fun generateVideoWithYoutubeContent(youtubeContent: YoutubeContentJpaEntity) =
    sut.giveMeBuilder<VideoJpaEntity>().set(VideoJpaEntity::youtubeContent, youtubeContent)
