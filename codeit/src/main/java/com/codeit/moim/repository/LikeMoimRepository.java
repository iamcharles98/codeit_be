package com.codeit.moim.repository;

import com.codeit.moim.model.LikeMoim;
import com.codeit.moim.model.ParticipatingMoim;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMoimRepository extends JpaRepository<LikeMoim, Long> {
    @Query("SELECT lm FROM LikeMoim lm JOIN FETCH lm.moim WHERE lm.user.id = :userId")
    List<LikeMoim> findLikeMoimByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(lm) from LikeMoim lm where lm.moim.id = :moimId")
    Integer countLikeMoimByMoimId(@Param("moimId") Long moimId);
}
