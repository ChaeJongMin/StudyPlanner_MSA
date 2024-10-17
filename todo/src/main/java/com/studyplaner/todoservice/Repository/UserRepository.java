package com.studyplaner.todoservice.Repository;

import com.studyplaner.todoservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserId(String target);
}
