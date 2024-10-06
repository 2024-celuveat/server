package com.celuveat.notification.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.notification.exception.NotFoundNotificationException
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationJpaRepository : JpaRepository<NotificationJpaEntity, Long> {
    override fun getById(id: Long): NotificationJpaEntity {
        return findByIdOrThrow(id) { NotFoundNotificationException }
    }

    fun findAllByMemberId(memberId: Long): List<NotificationJpaEntity>
}
