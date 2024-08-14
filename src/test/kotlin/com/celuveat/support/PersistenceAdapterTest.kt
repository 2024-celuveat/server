package com.celuveat.support

import com.celuveat.auth.adaptor.out.TokenAdaptor
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.annotation.Mapper
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
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [TokenAdaptor::class])]
)
@Import(JpaConfig::class)
@DataJpaTest
annotation class PersistenceAdapterTest
