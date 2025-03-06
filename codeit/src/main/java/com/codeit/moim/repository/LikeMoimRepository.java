package com.codeit.moim.repository;

import com.codeit.moim.model.LikeMoim;
import com.codeit.moim.model.Moim;
import com.codeit.moim.model.ParticipatingMoim;
import com.codeit.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeMoimRepository extends JpaRepository<LikeMoim, Long> {
    @Query("SELECT lm FROM LikeMoim lm JOIN FETCH lm.moim WHERE lm.user.id = :userId and lm.isDeleted = false")
    List<LikeMoim> findLikeMoimByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(lm) from LikeMoim lm where lm.moim.id = :moimId and lm.isDeleted = false ")
    Integer countLikeMoimByMoimId(@Param("moimId") Long moimId);

    @Modifying
    @Query("update LikeMoim l set l.isDeleted = ?1 where l.moim.id = ?2 and l.user.id = ?3")
    int updateIsDeletedByMoimAndUser(boolean isDeleted, Long moimId, Long userId);

    Optional<LikeMoim> findByMoim_IdAndUser_Id(Long id, Long id1);
}
