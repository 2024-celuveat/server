package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoPersistenceMapper
import com.celuveat.celeb.application.port.out.ReadVideoPort
import com.celuveat.celeb.domain.Video
import com.celuveat.common.annotation.Adapter

@Adapter
class VideoPersistenceAdapter(
    private val videoJpaRepository: VideoJpaRepository,
    private val videoPersistenceMapper: VideoPersistenceMapper,
) : ReadVideoPort {

    override fun readVideosInRestaurant(restaurantId: Long): List<Video> {
        val videos = videoJpaRepository.findByRestaurantId(restaurantId)
        return videos.map { videoPersistenceMapper.toDomain(it) }
    }
}
