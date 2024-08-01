package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface CelebrityJpaRepository : JpaRepository<CelebrityJpaEntity, Long>
