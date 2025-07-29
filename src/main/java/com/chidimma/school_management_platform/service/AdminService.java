package com.chidimma.school_management_platform.service;

import com.chidimma.school_management_platform.dto.*;
import com.chidimma.school_management_platform.model.SchoolClass;
import com.chidimma.school_management_platform.model.Student;
import com.chidimma.school_management_platform.model.Subject;
import com.chidimma.school_management_platform.model.Teacher;
import com.chidimma.school_management_platform.repository.SchoolClassRepository;
import com.chidimma.school_management_platform.repository.StudentRepository;
import com.chidimma.school_management_platform.repository.SubjectRepository;
import com.chidimma.school_management_platform.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;


    // To create users/classes:
    public SchoolClass createClass(CreateClassRequest classRequest) {
        if (schoolClassRepository.existsByName(classRequest.getName())){
            throw new RuntimeException("Class already exists");
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .name(classRequest.getName())
                .build());
    }

    public Student createStudent (CreateStudentRequest studentRequest) {
        SchoolClass schoolClass = schoolClassRepository.findById(studentRequest.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Student student = Student.builder()
                .name(studentRequest.getName())
                .admissionNumber(studentRequest.getAdmissionNumber())
                .schoolClass(schoolClass)
                .build();
        return studentRepository.save(student);
    }

    public ViewTeacherResponse createTeacher (CreateTeacherRequest teacherRequest) {

        SchoolClass schoolClass = schoolClassRepository.findById(teacherRequest.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        teacherRepository.findByNameIgnoreCaseAndStaffNumber(teacherRequest.getName(), teacherRequest.getStaffNumber()
        ).ifPresent(t -> {
            throw new RuntimeException("This teacher already exists");
        });
        subjectRepository.findByNameIgnoreCaseAndSchoolClassId(teacherRequest.getSubjectName(),teacherRequest.getClassId()
        ).ifPresent(s -> {
            throw new RuntimeException("Subject already created");
        });

        Teacher teacher = Teacher.builder()
                .name(teacherRequest.getName())
                .staffNumber(teacherRequest.getStaffNumber())
                .build();

        Teacher savedTeacher = teacherRepository.save(teacher);

        Subject subject = Subject.builder()
                .name(teacherRequest.getSubjectName())
                .schoolClass(schoolClass)
                .teacher(teacher)
                .build();

        Subject savedSubject = subjectRepository.save(subject);

        teacher.setSubject(savedSubject);

        ViewTeacherResponse response = new ViewTeacherResponse(
                teacher.getId(),
                teacher.getName(),
                teacher.getStaffNumber(),
                savedSubject.getName(),
                savedSubject.getSchoolClass().getName()
        );
        return response;
    }


    // To view all classes and users:
    public List<ViewSchoolClassResponse> getAllClasses(){
        return schoolClassRepository.findAll().stream()
                .map(c -> ViewSchoolClassResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ViewStudentResponse> getAllStudents(){
        return studentRepository.findAll().stream()
                .map(s -> ViewStudentResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .admissionNumber(s.getAdmissionNumber())
                        .className(s.getSchoolClass().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ViewTeacherResponse> getAllTeachers(){
        return teacherRepository.findAll().stream()
                .map(t -> {
                    Subject subject = t.getSubject();
                    String subjectName = subject != null ? subject.getName() : null;
                    String className = (subject != null && subject.getSchoolClass() != null)
                            ? subject.getSchoolClass().getName()
                            : null;

                    return ViewTeacherResponse.builder()
                            .id(t.getId())
                            .name(t.getName())
                            .staffNumber(t.getStaffNumber())
                            .subjectName(subjectName)
                            .className(className)
                            .build();
                })
                .collect(Collectors.toList());
    }

    //From a specific class
    public List<ViewStudentResponse> getStudentsByClass(Long classId) {
        return studentRepository.findBySchoolClassId(classId).stream()
                .map(s -> ViewStudentResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .admissionNumber(s.getAdmissionNumber())
                        .className(s.getSchoolClass().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ViewTeacherResponse> getTeacherByClass(Long classId) {
        return subjectRepository.findBySchoolClassId(classId).stream()
                .map(Subject::getTeacher)
                .distinct()
                .map(t -> ViewTeacherResponse.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .staffNumber(t.getStaffNumber())
                        .build())
                .collect(Collectors.toList());
    }
}