//package com.studyplanner.userservice.repository;
//
//import com.studyplanner.userservice.domain.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<UserEntity,Long> {
//    //닉네임으로 유저 찾기
//    Optional<UserEntity> findByNickname(String target);
//
//    //닉네임이나 아이디로 존재하는지
//    boolean existsByUserId(String target);
//    boolean existsByNickname(String target);
//
//    //삭제
//    void deleteByUserId(long target);
//}
