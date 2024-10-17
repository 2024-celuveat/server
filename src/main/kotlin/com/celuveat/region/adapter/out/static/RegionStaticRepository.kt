package com.celuveat.region.adapter.out.static

import com.celuveat.common.adapter.out.aws.CloudFrontProperty
import com.celuveat.common.annotation.Adapter
import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult

@Adapter
class RegionStaticRepository(
    property: CloudFrontProperty,
) {
    private val representedRegions: List<RepresentativeRegionResult> = listOf(
        RepresentativeRegionResult("잠실", "${property.domain}/regions/jamsil.webp"),
        RepresentativeRegionResult("성수", "${property.domain}/regions/seongsu.webp"),
        RepresentativeRegionResult("홍대", "${property.domain}/regions/hongdae.webp"),
        RepresentativeRegionResult("을지로", "${property.domain}/regions/euljiro.webp"),
        RepresentativeRegionResult("압구정", "${property.domain}/regions/apgujeong.webp"),
        RepresentativeRegionResult("여의도", "${property.domain}/regions/yeouido.webp"),
        RepresentativeRegionResult("이태원", "${property.domain}/regions/itaewon.webp"),
    )

    fun readRepresentativeRegions(): List<RepresentativeRegionResult> {
        return representedRegions
    }
}
