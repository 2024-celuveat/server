package com.celuveat.support

import com.celuveat.auth.adapter.out.TokenAdapter
import com.celuveat.common.adapter.out.persistence.JdslConfig
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.annotation.Mapper
import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ComponentScan(
    basePackages = ["com.celuveat"],
    useDefaultFilters = false,
    includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Adapter::class, Mapper::class])],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [TokenAdapter::class])],
)
@Import(JpaConfig::class, KotlinJdslAutoConfiguration::class, JdslConfig::class)
@DataJpaTest
annotation class PersistenceAdapterTest
