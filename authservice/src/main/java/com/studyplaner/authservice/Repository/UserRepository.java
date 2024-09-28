package com.studyplaner.authservice.Repository;

import com.studyplaner.authservice.Dto.UserDto;
import com.studyplaner.authservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByUserId(String target);
    Optional<UserDto> findByUserId(String userId);
}
