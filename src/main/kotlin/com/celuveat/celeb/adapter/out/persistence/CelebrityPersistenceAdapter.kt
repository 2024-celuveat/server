package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeChannelJpaRepository
import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.common.annotation.Adapter
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository

@Adapter
class CelebrityPersistenceAdapter(
    private val youtubeChannelJpaRepository: YoutubeChannelJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val celebrityPersistenceMapper: CelebrityPersistenceMapper,
) : FindCelebritiesPort {
    override fun findInterestedCelebrities(memberId: Long): List<Celebrity> {
        memberJpaRepository.getById(memberId)
        val celebrities = interestedCelebrityJpaRepository.findAllCelebritiesByMemberId(memberId)
        val celebrityIds = celebrities.map { it.id }
        val youtubeChannelsByCelebrity = youtubeChannelJpaRepository.findAllByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
        return celebrities.map { celebrityPersistenceMapper.toDomain(it, youtubeChannelsByCelebrity[it.id]!!) }
    }

    override fun findVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>> {
        val celebritiesWithRestaurant = restaurantInVideoJpaRepository.findVisitedCelebrities(restaurantIds)
        val celebrityIds = celebritiesWithRestaurant.map { it.celebrity.id }
        val youtubeChannelsByCelebrity = youtubeChannelJpaRepository.findAllByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
        return celebritiesWithRestaurant.groupBy { it.restaurantId } // restaurantId 별로 그룹핑
            .mapValues { (_, visitedCelebrities) -> // visitedCelebrities 를 celebrity 객체로 변환
                visitedCelebrities.map {
                    celebrityPersistenceMapper.toDomain(
                        it.celebrity,
                        youtubeChannelsByCelebrity[it.celebrity.id]!!,
                    )
                }
            }
    }
}
