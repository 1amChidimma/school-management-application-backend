package com.chidimma.school_management_platform.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTeacherRequest {
    private String name;
    private String staffNumber;
    private String subjectName;
    private Long classId;
}

