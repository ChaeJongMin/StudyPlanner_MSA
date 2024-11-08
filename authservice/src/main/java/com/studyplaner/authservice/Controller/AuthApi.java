package com.studyplaner.authservice.Controller;

import com.studyplaner.authservice.Dto.RequestIssueDto;
import com.studyplaner.authservice.Dto.RequestJoinUserDto;
import com.studyplaner.authservice.service.ResponseTokenUtil;
import com.studyplaner.authservice.service.TokenUtil;
import com.studyplaner.authservice.service.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthApi {

    private final UserServiceImpl userServiceImpl;
    private final ResponseTokenUtil responseTokenUtil;

    @PostMapping("/reissue")
    public ResponseEntity<?> postToken(@RequestBody RequestIssueDto requestIssueDto, HttpServletResponse httpServletResponse) throws IOException {
        log.info("토큰 재발급 요청이 왔습니다.");
        Cookie cookie = userServiceImpl.issueToken(requestIssueDto);

        httpServletResponse.addCookie(cookie);
        String json = responseTokenUtil.sendResponse(200, "AccessToken", "AccessToken is issued");

        return ResponseEntity.ok(json);
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable long userId){

        return null;
    }

}
