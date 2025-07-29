package com.chidimma.school_management_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String admissionNumber;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonIgnore
    private SchoolClass schoolClass;

}
