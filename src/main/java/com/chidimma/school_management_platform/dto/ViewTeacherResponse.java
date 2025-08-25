package com.chidimma.school_management_platform.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ViewTeacherResponse {
    private Long id;
    private String name;
    private String staffNumber;
    private String subjectName;
    private String className;
}
