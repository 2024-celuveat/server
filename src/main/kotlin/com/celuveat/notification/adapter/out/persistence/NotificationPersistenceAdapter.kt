package com.celuveat.notification.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.notification.adapter.out.persistence.entity.NotificationJpaRepository
import com.celuveat.notification.adapter.out.persistence.entity.NotificationPersistenceMapper
import com.celuveat.notification.application.port.out.ReadNotificationPort
import com.celuveat.notification.application.port.out.SaveNotificationPort
import com.celuveat.notification.domain.Notification

@Adapter
class NotificationPersistenceAdapter(
    private val notificationJpaRepository: NotificationJpaRepository,
    private val notificationPersistenceMapper: NotificationPersistenceMapper
) : ReadNotificationPort, SaveNotificationPort {
    override fun readById(id: Long): Notification {
        return notificationPersistenceMapper.toDomain(notificationJpaRepository.getById(id))
    }

    override fun readByMember(memberId: Long): List<Notification> {
        return notificationJpaRepository.findAllByMemberId(memberId)
            .map { notificationPersistenceMapper.toDomain(it) }
    }

    override fun saveNotification(notification: Notification) {
        val entity = notificationPersistenceMapper.toEntity(notification)
        notificationJpaRepository.save(entity)
    }
}
