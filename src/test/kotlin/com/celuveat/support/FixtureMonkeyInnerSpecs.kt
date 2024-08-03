package com.celuveat.support

import com.celuveat.celeb.domain.ChannelId
import com.navercorp.fixturemonkey.customizer.InnerSpec

fun randomString() = (5..8).map { ('a'..'z').random() }.joinToString("")

val channelIdSpec: InnerSpec = InnerSpec().property("channelId", ChannelId("@" + randomString()))
