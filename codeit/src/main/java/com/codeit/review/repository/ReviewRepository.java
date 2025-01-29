package com.codeit.review.repository;

import com.codeit.review.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r join fetch r.user u where r.moim.id = :moimId ")
    List<Review> findByMoimIdWithUser(@Param(value = "moimId") Long moimId);

    @Query("select r from Review r join fetch r.user u join fetch r.moim m where r.user.id = :userId ")
    List<Review> findByUserIdWithUser(@Param(value = "userId") Long userId);
}
