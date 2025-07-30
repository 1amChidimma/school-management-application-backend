package com.chidimma.school_management_platform.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTeacherRequest {
    private String name;
    private String staffNumber;
    private String subjectName;
    private Long classId;
}

