package com.celuveat.member.adapter.`in`.rest.config

import com.celuveat.member.adapter.`in`.rest.SocialLoginTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SocialLoginConfiguration(
    private val socialLoginTypeConverter: SocialLoginTypeConverter,
) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(socialLoginTypeConverter)
    }
}
