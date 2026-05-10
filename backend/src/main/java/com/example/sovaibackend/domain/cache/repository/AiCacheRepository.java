package com.example.sovaibackend.domain.cache.repository;

import com.example.sovaibackend.domain.cache.entity.AiCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiCacheRepository extends JpaRepository<AiCache, Long> {
    Optional<AiCache> findTopByOrderByCreatedAtDesc();
}
