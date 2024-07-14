package com.celuveat.auth.adaptor.`in`.rest.config

import com.celuveat.auth.adaptor.`in`.rest.AuthMemberArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfiguration(
    private val authMemberArgumentResolver: AuthMemberArgumentResolver,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authMemberArgumentResolver)
    }
}
