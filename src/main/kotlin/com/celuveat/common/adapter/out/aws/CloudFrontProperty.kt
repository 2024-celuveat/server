package com.celuveat.common.adapter.out.aws

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.cloudfront")
data class CloudFrontProperty(
    val domain: String,
)
