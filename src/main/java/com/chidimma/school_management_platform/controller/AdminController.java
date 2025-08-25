package com.chidimma.school_management_platform.controller;

import com.chidimma.school_management_platform.dto.*;
import com.chidimma.school_management_platform.model.SchoolClass;
import com.chidimma.school_management_platform.model.Student;
import com.chidimma.school_management_platform.model.Teacher;
import com.chidimma.school_management_platform.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create-class")
    public ResponseEntity<ViewSchoolClassResponse> createClass(@RequestBody CreateClassRequest classRequest){
        SchoolClass schoolClass = adminService.createClass(classRequest);
        return ResponseEntity.ok(ViewSchoolClassResponse.builder()
                .id(schoolClass.getId())
                .name(schoolClass.getName())
                .build()
        );
    }

    @PostMapping("/create-student")
    public ResponseEntity<ViewStudentResponse> createStudent(@RequestBody CreateStudentRequest studentRequest){
        Student student = adminService.createStudent(studentRequest);
        return ResponseEntity.ok(ViewStudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .admissionNumber(student.getAdmissionNumber())
                .className(student.getSchoolClass().getName())
                .build()
        );
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<ViewTeacherResponse> createTeacher(@RequestBody CreateTeacherRequest request) {
        ViewTeacherResponse response = adminService.createTeacher(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/classes")
    public ResponseEntity<List<ViewSchoolClassResponse>> getAllClasses(){
        return ResponseEntity.ok(adminService.getAllClasses());
    }

    @GetMapping("/students")
    public ResponseEntity<List<ViewStudentResponse>> getAllStudents(){
        List<ViewStudentResponse> students = adminService.getAllStudents();
        System.out.println(">>> getAllStudents response: " + students);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<ViewTeacherResponse>> getAllTeachers(){
        List<ViewTeacherResponse> teachers = adminService.getAllTeachers();
        System.out.println(">>> getAllStudents response: " + teachers);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/classes/{classId}/students")
    public ResponseEntity<List<ViewStudentResponse>> getStudentsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(adminService.getStudentsByClass(classId));
    }

    @GetMapping("/classes/{classId}/teachers")
    public ResponseEntity<List<ViewTeacherResponse>> getTeachersByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(adminService.getTeacherByClass(classId));
    }
}