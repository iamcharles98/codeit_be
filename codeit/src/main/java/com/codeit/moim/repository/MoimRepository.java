package com.codeit.moim.repository;

import com.codeit.moim.model.Moim;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    @Query("select m from Moim m join fetch m.moimLocation ml where m.id = :moimId")
    Optional<Moim> findById(@Param("moimId") Long moimId);

    @Query("select m from Moim m join fetch m.moimLocation ml")
    List<Moim> findAllWithLocation();

    @Query(value = "SELECT m FROM Moim m JOIN FETCH m.moimLocation ml",
            countQuery = "SELECT COUNT(m) FROM Moim m")
    Page<Moim> findAllWithLocation(Pageable pageable);
}
