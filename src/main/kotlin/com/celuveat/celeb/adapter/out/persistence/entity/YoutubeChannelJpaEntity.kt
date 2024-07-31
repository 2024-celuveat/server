package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class YoutubeChannelJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val channelId: String,
    val channelUrl: String,
    val channelName: String,
    val contentsName: String,
    val contentsIntroduction: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val celebrity: CelebrityJpaEntity,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
