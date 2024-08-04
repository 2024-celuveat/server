package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaRepository
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(CelebrityPersistenceAdapter::class, CelebrityPersistenceMapper::class, JpaConfig::class)
@DataJpaTest
class CelebrityPersistenceAdapterTest(
    private val celebrityPersistenceAdapter: CelebrityPersistenceAdapter,
    private val memberJpaRepository: MemberJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val videoJpaRepository: VideoJpaRepository,
) : StringSpec({
    "회원이 관심 목록에 추가한 셀럽을 조회 한다." {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::celebrity, celebrityA)
            .sampleList(2)
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .set(YoutubeContentJpaEntity::celebrity, celebrityB)
            .sample()
        youtubeContentJpaRepository.saveAll(contentA + contentB)
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        interestedCelebrityJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, celebrityA)
                    .sample(),
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, celebrityB)
                    .sample(),
            ),
        )

        // when
        val celebrities = celebrityPersistenceAdapter.findInterestedCelebrities(savedMember.id)

        // then
        assertSoftly {
            celebrities.size shouldBe 2
            celebrities.forAll { it.youtubeContents shouldNotBe null }
        }
    }

    "식당을 방문한 셀럽을 조회 한다." {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelAId")
            .set(YoutubeContentJpaEntity::celebrity, celebrityA)
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelBId")
            .set(YoutubeContentJpaEntity::celebrity, celebrityB)
            .sample()
        val youtubeContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB))

        val savedVideos = videoJpaRepository.saveAll(
            listOf(
                generateVideoWithYoutubeContent(youtubeContents[0]).sample(),
                generateVideoWithYoutubeContent(youtubeContents[1]).sample(),
                generateVideoWithYoutubeContent(youtubeContents[1]).sample(),
            ),
        )

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
        )
        // fixture
        // [셀럽A]-[채널A]-[영상1], [셀럽B]-[채널B]-[영상2]에서 [식당1] 방문
        // [셀럽B]-[채널B]-[영상3]은 [식당2] 방문

        // when
        val visitedCelebritiesByRestaurants =
            celebrityPersistenceAdapter.findVisitedCelebritiesByRestaurants(restaurants.map { it.id })

        // then
        assertSoftly {
            visitedCelebritiesByRestaurants.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[0].id]!!.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[1].id]!!.size shouldBe 1
        }
    }
})

private fun generateVideoWithYoutubeContent(youtubeContent: YoutubeContentJpaEntity) =
    sut.giveMeBuilder<VideoJpaEntity>().set(VideoJpaEntity::youtubeContent, youtubeContent)
