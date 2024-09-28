package com.celuveat.notification.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface NotificationJpaRepository : JpaRepository<NotificationJpaEntity, Long> {
    fun findAllByMemberId(memberId: Long): List<NotificationJpaEntity>
}
