package com.chidimma.school_management_platform.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ViewStudentResponse {
    private Long id;
    private String name;
    private String admissionNumber;
    private String className;
}
