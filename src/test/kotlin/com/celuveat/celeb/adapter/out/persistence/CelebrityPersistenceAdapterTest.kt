package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoFeaturedRestaurantJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoFeaturedRestaurantJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeChannelJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeChannelJpaRepository
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
    private val youtubeChannelJpaRepository: YoutubeChannelJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val videoFeaturedRestaurantJpaRepository: VideoFeaturedRestaurantJpaRepository,
    private val videoJpaRepository: VideoJpaRepository,
) : StringSpec({
    "회원이 관심 목록에 추가한 셀럽을 조회 한다." {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val channelA = sut.giveMeBuilder<YoutubeChannelJpaEntity>()
            .set(YoutubeChannelJpaEntity::id, 0)
            .set(YoutubeChannelJpaEntity::channelId, "@channelId")
            .set(YoutubeChannelJpaEntity::celebrity, celebrityA)
            .sampleList(2)
        val channelB = sut.giveMeBuilder<YoutubeChannelJpaEntity>()
            .set(YoutubeChannelJpaEntity::id, 0)
            .set(YoutubeChannelJpaEntity::channelId, "@channelId")
            .set(YoutubeChannelJpaEntity::celebrity, celebrityB)
            .sample()
        youtubeChannelJpaRepository.saveAll(channelA + channelB)
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
            celebrities.forAll { it.youtubeChannels shouldNotBe null }
        }
    }

    "식당을 방문한 셀럽을 조회 한다." {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val channelA = sut.giveMeBuilder<YoutubeChannelJpaEntity>()
            .set(YoutubeChannelJpaEntity::channelId, "@channelAId")
            .set(YoutubeChannelJpaEntity::celebrity, celebrityA)
            .sample()
        val channelB = sut.giveMeBuilder<YoutubeChannelJpaEntity>()
            .set(YoutubeChannelJpaEntity::channelId, "@channelBId")
            .set(YoutubeChannelJpaEntity::celebrity, celebrityB)
            .sample()
        val youtubeChannels = youtubeChannelJpaRepository.saveAll(listOf(channelA, channelB))

        val savedVideos = videoJpaRepository.saveAll(
            listOf(
                generateVideoWithYoutubeChannel(youtubeChannels[0]).sample(),
                generateVideoWithYoutubeChannel(youtubeChannels[1]).sample(),
                generateVideoWithYoutubeChannel(youtubeChannels[1]).sample(),
            ),
        )

        val restaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        videoFeaturedRestaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<VideoFeaturedRestaurantJpaEntity>()
                    .set(VideoFeaturedRestaurantJpaEntity::video, savedVideos[0])
                    .set(VideoFeaturedRestaurantJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<VideoFeaturedRestaurantJpaEntity>()
                    .set(VideoFeaturedRestaurantJpaEntity::video, savedVideos[1])
                    .set(VideoFeaturedRestaurantJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<VideoFeaturedRestaurantJpaEntity>()
                    .set(VideoFeaturedRestaurantJpaEntity::video, savedVideos[2])
                    .set(VideoFeaturedRestaurantJpaEntity::restaurant, restaurants[1])
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

private fun generateVideoWithYoutubeChannel(youtubeChannel: YoutubeChannelJpaEntity) =
    sut.giveMeBuilder<VideoJpaEntity>().set(VideoJpaEntity::youtubeChannel, youtubeChannel)
