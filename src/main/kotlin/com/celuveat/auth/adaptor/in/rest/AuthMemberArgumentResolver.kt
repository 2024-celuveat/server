package com.celuveat.auth.adaptor.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.common.adapter.out.rest.getTokenAuthorizationOrThrow
import com.celuveat.common.adapter.out.rest.toHttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthMemberArgumentResolver(
    val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthId::class.java)
                && parameter.parameterType == Long::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val httpServletRequest = webRequest.toHttpServletRequest()
        val authorization = httpServletRequest.getTokenAuthorizationOrThrow()
        return extractMemberIdUseCase.extract(authorization)
    }
}
