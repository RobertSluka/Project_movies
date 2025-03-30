package dev.sluka.movies.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.sluka.movies.Entity.Movie;
import dev.sluka.movies.Entity.User;
import jakarta.transaction.Transactional;
// I am using JPQL instead of nativeQuery which would force me to match the database columns exacatly

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);

    User findByEmail(String email);

    Boolean existsByUserName(String userName);

    void deleteByUserName(String userName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userName = :username")
    void updatePassword(@Param("username") String username, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = false, u.lockTime = :lockTime WHERE u.userName = :username")
    int lockUser(@Param("lockTime") java.sql.Timestamp lockTime, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = true, u.failedAttempt = 0, u.lockTime = null WHERE u.userName = :username")
    void unlockUser(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :failedAttempt WHERE u.userName = :username")
    void updateFailedAttempts(@Param("failedAttempt") int failedAttempt, @Param("username") String username);
}