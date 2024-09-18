package com.celuveat.region.adapter.out.persistence

import com.celuveat.region.adapter.out.persistence.entity.RegionJpaEntity
import com.celuveat.region.adapter.out.persistence.entity.RegionJpaRepository
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@PersistenceAdapterTest
class RegionPersistenceAdapterTest(
    private val regionPersistenceAdapter: RegionPersistenceAdapter,
    private val regionJpaRepository: RegionJpaRepository,
) : FunSpec({
    test("지역 이름으로 검색한다.") {
        // given
        regionJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RegionJpaEntity>()
                    .setExp(RegionJpaEntity::name, "서울 도봉구")
                    .sample(),
                sut.giveMeBuilder<RegionJpaEntity>()
                    .setExp(RegionJpaEntity::name, "서울 노원구")
                    .sample(),
                sut.giveMeBuilder<RegionJpaEntity>()
                    .setExp(RegionJpaEntity::name, "경기 순천")
                    .sample(),
                sut.giveMeBuilder<RegionJpaEntity>()
                    .setExp(RegionJpaEntity::name, "전남 순천")
                    .sample(),
            ),
        )

        // when
        val result1 = regionPersistenceAdapter.readByName("서울")
        val result2 = regionPersistenceAdapter.readByName("순천")
        val result3 = regionPersistenceAdapter.readByName("도봉")

        // then
        result1.size shouldBe 2
        result2.size shouldBe 2
        result3.size shouldBe 1
    }
})
