package com.sparta.business.domain.master.repository;

import com.sparta.business.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
    Optional<Region> findByRegionName(String regionName);

    List<Region> findByDeletedAtIsNull();
}
