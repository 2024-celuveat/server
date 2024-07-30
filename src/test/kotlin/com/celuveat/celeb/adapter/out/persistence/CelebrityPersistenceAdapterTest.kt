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
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        val channels = sut.giveMeBuilder<YoutubeChannelJpaEntity>()
            .set(YoutubeChannelJpaEntity::id, 0)
            .set(YoutubeChannelJpaEntity::channelId, "@channelId")
            .sampleList(3)
        val savedChannels = youtubeChannelJpaRepository.saveAll(channels)
        val savedCelebrities = celebrityJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<CelebrityJpaEntity>()
                    .set(CelebrityJpaEntity::youtubeChannels, savedChannels.subList(0, 2))
                    .sample(),
                sut.giveMeBuilder<CelebrityJpaEntity>()
                    .set(CelebrityJpaEntity::youtubeChannels, savedChannels.subList(1, 3))
                    .sample(),
            ),
        )

        interestedCelebrityJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, savedCelebrities[0])
                    .sample(),
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, savedCelebrities[1])
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
