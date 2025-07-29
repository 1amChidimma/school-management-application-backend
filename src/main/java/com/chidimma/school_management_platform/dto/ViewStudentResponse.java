package com.chidimma.school_management_platform.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewStudentResponse {
    private Long id;
    private String name;
    private String admissionNumber;
    private String className;
}
