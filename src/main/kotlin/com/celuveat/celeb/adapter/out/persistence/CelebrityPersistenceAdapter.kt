package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.common.annotation.Adapter

@Adapter
class CelebrityPersistenceAdapter(
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val celebrityPersistenceMapper: CelebrityPersistenceMapper,
) : ReadCelebritiesPort {
    override fun readVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>> {
        val celebritiesWithRestaurant = restaurantInVideoJpaRepository.findVisitedCelebrities(restaurantIds)
        val celebrityIds = celebritiesWithRestaurant.map { it.celebrity.id }
        val youtubeContentsByCelebrity = celebritiesToContentMap(celebrityIds)
        return celebritiesWithRestaurant.groupBy { it.restaurantId } // restaurantId 별로 그룹핑
            .mapValues { (_, visitedCelebrities) -> // visitedCelebrities 를 celebrity 객체로 변환
                visitedCelebrities.map {
                    celebrityPersistenceMapper.toDomain(
                        it.celebrity,
                        youtubeContentsByCelebrity[it.celebrity.id]!!,
                    )
                }.distinct()
            }
    }

    private fun celebritiesToContentMap(celebrityIds: List<Long>): Map<Long, List<YoutubeContentJpaEntity>> =
        celebrityYoutubeContentJpaRepository.findByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
            .mapValues { (_, celebrityYoutubeContents) -> celebrityYoutubeContents.map { it.youtubeContent } }

    override fun readBestCelebrities(): List<Celebrity> {
        return celebrityJpaRepository.findAllBySubscriberCountDescTop10().map {
            celebrityPersistenceMapper.toDomainWithoutYoutubeContent(it)
        }
    }

    override fun readById(celebrityId: Long): Celebrity {
        val celebrity = celebrityJpaRepository.getById(celebrityId)
        val youtubeContents = celebrityYoutubeContentJpaRepository.findByCelebrity(celebrity)
            .map { it.youtubeContent }
        return celebrityPersistenceMapper.toDomain(celebrity, youtubeContents)
    }
}
