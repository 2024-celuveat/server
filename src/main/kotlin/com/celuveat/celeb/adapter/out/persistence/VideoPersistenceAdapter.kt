package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoPersistenceMapper
import com.celuveat.celeb.application.port.out.ReadVideoPort
import com.celuveat.celeb.domain.Video
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.utils.throwWhen
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.exception.NotFoundRestaurantException

@Adapter
class VideoPersistenceAdapter(
    private val videoJpaRepository: VideoJpaRepository,
    private val videoPersistenceMapper: VideoPersistenceMapper,
    private val restaurantJpaRepository: RestaurantJpaRepository,
) : ReadVideoPort {

    override fun readVideosInRestaurant(restaurantId: Long): List<Video> {
        throwWhen(!restaurantJpaRepository.existsById(restaurantId)) { NotFoundRestaurantException }
        val videos = videoJpaRepository.findByRestaurantId(restaurantId)
        return videos.map { videoPersistenceMapper.toDomain(it) }
    }
}
