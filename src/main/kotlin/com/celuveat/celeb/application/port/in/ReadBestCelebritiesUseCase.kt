package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.result.SimpleCelebrityResult

interface ReadBestCelebritiesUseCase {
    fun readBestCelebrities(): List<SimpleCelebrityResult>
}
