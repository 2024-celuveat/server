package com.celuveat.support

import com.celuveat.common.adapter.out.aws.CloudFrontProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(value = [CloudFrontProperty::class])
@Configuration
class EnablePropertiesConfiguration
