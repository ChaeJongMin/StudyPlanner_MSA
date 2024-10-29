package com.studyplanner.userservice.service;

import com.studyplanner.userservice.Exception.CustomException;
import com.studyplanner.userservice.Exception.DuplicateUserInfo;
import com.studyplanner.userservice.Feign.StatisticServiceClient;
import com.studyplanner.userservice.MessageQueue.KafkaProducer;
import com.studyplanner.userservice.MessageQueue.KafkaSendDto;
import com.studyplanner.userservice.domain.UserEntity;
import com.studyplanner.userservice.dto.request.RequestSaveUserDto;
import com.studyplanner.userservice.dto.request.RequsetStatisticDto;
import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import com.studyplanner.userservice.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Slf4j
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;
    private final StatisticServiceClient statisticServiceClient;

//    @Transactional
    @Override
    public Long save(RequestSaveUserDto requestSaveUserDto) {

        String userId = requestSaveUserDto.getUserId();
        String nickname = requestSaveUserDto.getNickname();
        //아이디 닉네임 중복 예외 처리
        if(userRepository.existsByUserId(userId)){
            throw new DuplicateUserInfo("Duplicate User-id", "The user-id is already in use by another user.");
        }
        if(userRepository.existsByNickname(nickname)){
            throw new DuplicateUserInfo("Duplicate Nickname", "The nickname is already in use by another user.");
        }
        String password = requestSaveUserDto.getPassword();
        //객체 생성 후 save
        requestSaveUserDto.encodePassword(passwordEncoder.encode(password));

        UserEntity userEntity = userRepository.save(requestSaveUserDto.toUserEntity());

        //feign 클라이언트로 전달
        log.info("[Jong]auth-service한테 회원가입한 유저 정보 전달");
        kafkaProducer.sendRegisterUser(new ResponseJoinForAuthServerDto(userId, requestSaveUserDto.getPassword()));
        return 1L;

    }

    @Override
    public ResponseJoinForAuthServerDto getUserByNickname(String nickname) {

        return null;
    }

    @Transactional
    @Override
    public Long update(long id,String nickname) {

        UserEntity userEntity= userRepository.findById(id)
                .orElseThrow(()-> new CustomException(HttpStatus.BAD_REQUEST,"Not_Found_User","해당 유저는 없습니다."));


        userEntity.update(nickname);
        kafkaProducer.sendUpdateNicKName(new KafkaSendDto(id,nickname));

        return 1L;
    }

    @Override
    @CircuitBreaker(name = "user-statistic-circuit-breaker", fallbackMethod = "defaultStatistic")
    public RequsetStatisticDto getStatistic(long id) {

        return statisticServiceClient.requestGetMultipleStatisticInformation(id);
    }

    @Transactional
    @Override
    public Long delete(long id) {

        UserEntity userEntity= userRepository.findById(id)
                .orElseThrow(()-> new CustomException(HttpStatus.BAD_REQUEST,"Not_Found_User","해당 유저는 없습니다."));

        userRepository.delete(userEntity);

        return 0L;
    }

    private RequsetStatisticDto defaultStatistic(){

        return RequsetStatisticDto.builder()
                .totalSuccessRate(0.0)
                .dailySuccessRate(0.0)
                .weeklySuccessRate(0.0)
                .monthlySuccessRate(0.0)
                .totalCount(0)
                .successCount(0)
                .failCount(0)
                .build();
    }
}
