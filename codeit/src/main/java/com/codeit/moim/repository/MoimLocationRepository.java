package com.codeit.moim.repository;

import com.codeit.moim.model.MoimLocation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoimLocationRepository extends JpaRepository<MoimLocation, Long> {
    Optional<MoimLocation> findByMoimId(Long moimId);
}
