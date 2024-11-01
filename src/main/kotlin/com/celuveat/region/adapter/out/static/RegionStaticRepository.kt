package com.celuveat.region.adapter.out.static

import com.celuveat.common.adapter.out.aws.CloudFrontProperty
import com.celuveat.common.annotation.Adapter
import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult

@Adapter
class RegionStaticRepository(
    property: CloudFrontProperty,
) {
    private val representedRegions: List<RepresentativeRegionResult> = listOf(
        RepresentativeRegionResult(
            name = "잠실",
            imageUrl = "${property.domain}/regions/jamsil.webp",
            latitude = 37.5067945,
            longitude = 127.0830482
        ),
        RepresentativeRegionResult(
            name = "성수",
            imageUrl = "${property.domain}/regions/seongsu.webp",
            latitude = 37.5436099,
            longitude = 127.0428194
        ),
        RepresentativeRegionResult(
            name = "홍대",
            imageUrl = "${property.domain}/regions/hongdae.webp",
            latitude = 37.5507254,
            longitude = 126.9256382
        ),
        RepresentativeRegionResult(
            name = "을지로",
            imageUrl = "${property.domain}/regions/euljiro.webp",
            latitude = 37.5660286,
            longitude = 126.9954924
        ),
        RepresentativeRegionResult(
            name = "압구정",
            imageUrl = "${property.domain}/regions/apgujeong.webp",
            latitude = 37.5271478,
            longitude = 127.0334517
        ),
        RepresentativeRegionResult(
            name = "여의도",
            imageUrl = "${property.domain}/regions/yeouido.webp",
            latitude = 37.5295808,
            longitude = 126.9326803
        ),
        RepresentativeRegionResult(
            name = "이태원",
            imageUrl = "${property.domain}/regions/leetaewon.webp",
            latitude = 37.5385051,
            longitude = 126.9925224
        ),
    )

    fun readRepresentativeRegions(): List<RepresentativeRegionResult> {
        return representedRegions
    }
}
