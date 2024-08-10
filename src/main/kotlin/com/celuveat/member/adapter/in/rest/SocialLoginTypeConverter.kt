package com.celuveat.member.adapter.`in`.rest

import com.celuveat.member.domain.SocialLoginType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class SocialLoginTypeConverter : Converter<String, SocialLoginType> {
    override fun convert(source: String): SocialLoginType {
        return SocialLoginType.valueOf(source.uppercase())
    }
}
