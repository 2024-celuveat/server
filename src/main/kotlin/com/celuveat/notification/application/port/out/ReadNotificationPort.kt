package com.celuveat.notification.application.port.out

import com.celuveat.notification.domain.Notification

interface ReadNotificationPort {
    fun readByMember(memberId: Long): List<Notification>
}
