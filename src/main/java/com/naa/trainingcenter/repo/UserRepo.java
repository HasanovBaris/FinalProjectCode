package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);


    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.name LIKE %:roleName%")
    List<User> findByRoleName(@Param("roleName") String roleName);

    List<User> getAllByIsEnabledAndUsernameIsNot(Boolean isEnabled, String username);
}