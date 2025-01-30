package com.codeit.moim.repository;

import com.codeit.moim.model.Moim;
import com.codeit.moim.model.ParticipatingMoim;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipatingMoimRepository extends JpaRepository<ParticipatingMoim, Long> {

    @Query("select COUNT(p) FROM ParticipatingMoim p where p.moim.id = :moimId")
    Integer findParticipantsByMoimId(@Param("moimId") Long moimId);

    @Query("select p FROM ParticipatingMoim  p join fetch p.user where p.moim.id = :moimId")
    List<ParticipatingMoim> findAllParticipantsByMoimId(@Param("moimId") Long moimId);

    @Query("SELECT pm FROM ParticipatingMoim pm JOIN FETCH pm.moim WHERE pm.user.id = :userId")
    List<ParticipatingMoim> findParticipatingMoimByUserId(@Param("userId") Long userId);

    @Query("SELECT pm FROM ParticipatingMoim pm JOIN FETCH pm.moim WHERE pm.user.id = :userId AND pm.isHost = true")
    List<ParticipatingMoim> findHostingMoimsByUserId(@Param("userId") Long userId);
}
