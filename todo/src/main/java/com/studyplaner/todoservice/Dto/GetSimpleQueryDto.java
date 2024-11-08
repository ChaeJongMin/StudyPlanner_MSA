package com.studyplaner.todoservice.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetSimpleQueryDto {

    private long id;
    private String content;
    private boolean isComplete;
    private int day;

    public GetSimpleQueryDto(long id, String content, boolean isComplete, int day){
        this.id = id;
        this.content = content;
        this.isComplete =isComplete;
        this.day= day;
    }


}
