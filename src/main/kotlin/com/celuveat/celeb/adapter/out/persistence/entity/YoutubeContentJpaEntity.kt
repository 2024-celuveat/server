package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "youtube_content")
@Entity
class YoutubeContentJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val contentsName: String,
    val channelId: String,
    val channelUrl: String,
    val channelName: String,
    val contentsIntroduction: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
