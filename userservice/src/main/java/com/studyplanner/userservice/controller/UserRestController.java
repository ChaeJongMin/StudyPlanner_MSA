package com.studyplanner.userservice.controller;

import com.studyplanner.userservice.dto.request.RequestSaveUserDto;
import com.studyplanner.userservice.dto.request.RequestUpdateUserDto;
import com.studyplanner.userservice.dto.request.RequsetStatisticDto;
import com.studyplanner.userservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpl userService;

    @GetMapping("/health_check")
    public String status() {
        return "Good Working";
    }

    //회원가입
    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody RequestSaveUserDto requestSaveUserDto){
        log.info("회원가입 요청 받음");
        userService.save(requestSaveUserDto);
        Map<String, String> response = new HashMap<>();
        response.put("code", "success");
        response.put("message", "success save user");

        return ResponseEntity.ok(response);
    }

    //조회
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findUser(@PathVariable Long id){
        return ResponseEntity.ok("success find user");
    }

    //수정
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RequestUpdateUserDto user){

        userService.update(id,user.getNickname());
        return ResponseEntity.ok("success update user");

    }

    @GetMapping("/user/myPage/{id}")
    public ResponseEntity<RequsetStatisticDto> getMyPageInfo(@PathVariable Long id){
        RequsetStatisticDto requsetStatisticDto = userService.getStatistic(id);
        return ResponseEntity.ok(requsetStatisticDto);

    }

    //삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok("success delete user");
    }
}
