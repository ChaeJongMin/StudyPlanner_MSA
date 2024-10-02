package com.studyplanner.userservice.Feign;

import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="auth-service")
public interface AuthServiceClient {

    @PostMapping("/users")
    void responseSuccessJoin(ResponseJoinForAuthServerDto responseDto);
}
