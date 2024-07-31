package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeChannelJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeChannelJpaRepository
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
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
})
