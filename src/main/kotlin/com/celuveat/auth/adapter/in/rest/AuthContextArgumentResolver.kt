package com.celuveat.auth.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.common.adapter.`in`.rest.getTokenAuthorizationOrNull
import com.celuveat.common.adapter.`in`.rest.toHttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthContextArgumentResolver(
    private val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Auth::class.java) &&
                parameter.parameterType == AuthContext::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val httpServletRequest = webRequest.toHttpServletRequest()
        return httpServletRequest.getTokenAuthorizationOrNull()
            ?.let { AuthContext(extractMemberIdUseCase.extract(it)) }
            ?: AuthContext.guest()
    }
}
