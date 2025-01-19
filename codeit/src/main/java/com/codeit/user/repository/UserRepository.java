package com.codeit.user.repository;

import com.codeit.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    @Query("select u from User u join fetch u.tags where u.id = :userId")
    Optional<User> findByIdWithTags(@Param("userId") Long id);
}
