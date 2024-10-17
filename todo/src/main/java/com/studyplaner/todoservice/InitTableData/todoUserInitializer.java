package com.studyplaner.todoservice.InitTableData;

import com.studyplaner.todoservice.Entity.UserEntity;
import com.studyplaner.todoservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class todoUserInitializer implements ApplicationRunner {

    final private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserEntity userEntity1 = new UserEntity(1L,"test123");
        UserEntity userEntity2 = new UserEntity(2L,"test456");
        UserEntity userEntity3 = new UserEntity(3L,"test789");

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);
        userRepository.save(userEntity3);
    }
}
