package com.celuveat.auth.adapter.`in`.rest.config

import com.celuveat.auth.adapter.`in`.rest.AuthContextArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfiguration(
    private val authContextArgumentResolver: AuthContextArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authContextArgumentResolver)
    }
}
