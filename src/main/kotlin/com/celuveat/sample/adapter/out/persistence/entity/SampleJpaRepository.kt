package com.celuveat.sample.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository : JpaRepository<SampleJpaEntity, Long>
