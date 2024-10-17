package com.studyplaner.todoservice.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestGetMonth {

    private String monthFormat;
    private String userId;
}
