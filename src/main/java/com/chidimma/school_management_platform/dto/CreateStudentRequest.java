package com.chidimma.school_management_platform.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {
    private String name;
    private String admissionNumber;
    private Long classId;
}
