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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SchoolClassRepository schoolClassRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getAllTeachers_shouldReturnCorrectTeacherResponse() {
        //ARRANGE:
        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 1").build();
        Subject subject = Subject.builder().id(1L).name("Mathematics").schoolClass(schoolClass).build();
        Teacher teacher = Teacher.builder().id(1L).name("Mr. John").staffNumber("T123").subject(subject).build();
        subject.setTeacher(teacher);

        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        //ACT:
        List<ViewTeacherResponse> responses = adminService.getAllTeachers();

        //ASSERT:
        assertEquals(1, responses.size());

        ViewTeacherResponse response = responses.get(0);
        assertEquals("Mr. John", response.getName());
        assertEquals("T123", response.getStaffNumber());
        assertEquals("Mathematics", response.getSubjectName());
        assertEquals("Primary 1", response.getClassName());
    }

    @Test
    void getAllSchoolClasses_shouldReturnCorrectSchoolClassResponse(){
        //ARRANGE:
        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 2").build();

        when(schoolClassRepository.findAll()).thenReturn(List.of(schoolClass));

        //ACT:
        List<ViewSchoolClassResponse> responses = adminService.getAllClasses();

        //ASSERT:
        assertEquals(1, responses.size());

        ViewSchoolClassResponse response = responses.get(0);
        assertEquals("Primary 2", response.getName());
    }

    @Test
    void getAllSchoolClasses_shouldReturnEmptyList_whenNoClassExists() {
        when(schoolClassRepository.findAll()).thenReturn(Collections.emptyList());
        List<ViewSchoolClassResponse> responses = adminService.getAllClasses();;
        assertTrue(responses.isEmpty());
    }

    @Test
    void getAllStudents_shouldReturnCorrectStudentResponse() {
        //ARRANGE:
        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 1").build();
        Student student = Student.builder().id(1L).name("James").admissionNumber("S123").schoolClass(schoolClass).build();

        when(studentRepository.findAll()).thenReturn(List.of(student));

        //ACT:
        List<ViewStudentResponse> responses = adminService.getAllStudents();

        //ASSERT:
        assertEquals(1, responses.size());

        ViewStudentResponse response = responses.get(0);
        assertEquals("James", response.getName());
        assertEquals("S123", response.getAdmissionNumber());
        assertEquals("Primary 1", response.getClassName());
    }

    @Test
    void getAllStudents_shouldReturnEmptyList_whenNoStudentsExist() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        List<ViewStudentResponse> responses = adminService.getAllStudents();

        assertTrue(responses.isEmpty());
    }

    @Test
    void getStudentByClass_shouldReturnStudentsForClass() {
        //ARRANGE:
        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 3").build();
        Student student = Student.builder().id(1L).name("Jane").admissionNumber("P300").schoolClass(schoolClass).build();

        when(studentRepository.findBySchoolClassId(1L)).thenReturn(List.of(student));

        //ACT:
        List<ViewStudentResponse> responses = adminService.getStudentsByClass(1L);

        //ASSERT:
        assertEquals(1, responses.size());
        ViewStudentResponse response = responses.get(0);
        assertEquals("Jane", response.getName());
        assertEquals("P300", response.getAdmissionNumber());
        assertEquals("Primary 3", response.getClassName());
    }

    @Test
    void getTeachersByClass_shouldReturnDistinctTeachers() {
        //ARRANGE:
        SchoolClass schoolClass = SchoolClass.builder().id(2L).name("Primary 4").build();
        Teacher teacher1 = Teacher.builder().id(1L).name("Mr. Obi").staffNumber("T001").build();
        Teacher teacher2 = Teacher.builder().id(1L).name("Mrs. Obi").staffNumber("T002").build();
        Subject subject1 = Subject.builder().id(1L).name("Math").schoolClass(schoolClass).teacher(teacher1).build();
        Subject subject2 = Subject.builder().id(2L).name("English").schoolClass(schoolClass).teacher(teacher2).build();

        when(subjectRepository.findBySchoolClassId(2L)).thenReturn(List.of(subject1, subject2));

        //ACT:
        List<ViewTeacherResponse> responses = adminService.getTeacherByClass(2L);

        //ASSERT:
        assertEquals(2, responses.size());
        ViewTeacherResponse response1 = responses.get(0);
        ViewTeacherResponse response2 = responses.get(1);
        assertEquals("Mr. Obi", response1.getName());
        assertEquals("T001", response1.getStaffNumber());
        assertEquals("Math", response1.getSubjectName());
        assertEquals("Primary 4", response1.getClassName());

        assertEquals("Mrs. Obi", response2.getName());
        assertEquals("T002", response2.getStaffNumber());
        assertEquals("English", response2.getSubjectName());
        assertEquals("Primary 4", response2.getClassName());

    }

    @Test
    void createStudent_shouldReturnSavedStudent() {
        //ARRANGE:
        CreateStudentRequest request = new CreateStudentRequest();
        request.setName("Apple");
        request.setAdmissionNumber("S001");
        request.setClassId(1L);

        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 1").build();

        // Builds a Student object just like the one that will be created by the method before it's saved to the database. This simulates the "pre-save" state — it has no ID yet.
        Student studentToSave = Student.builder().name("Ada").admissionNumber("STU123").schoolClass(schoolClass).build();

        // Simulates the state of the student after being saved — now with an assigned ID from the database.
        Student savedStudent = Student.builder().id(1L).name("Ada").admissionNumber("STU123").schoolClass(schoolClass).build();

        // Mocks search for and  return of the school class with the specific id involved
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        // Mocks the save operation for the student
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);


        // ACT:
        Student result = adminService.createStudent(request);

        // ASSERT:
        assertNotNull(result);
        assertEquals("Ada", result.getName());
        assertEquals("STU123", result.getAdmissionNumber());
        assertEquals("Primary 1", result.getSchoolClass().getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void createTeacher_shouldReturnSavedTeacherResponse() {
        // ARRANGE
        CreateTeacherRequest request = new CreateTeacherRequest();
        request.setName("Mr. John");
        request.setStaffNumber("TEA123");
        request.setSubjectName("Mathematics");
        request.setClassId(1L);

        SchoolClass schoolClass = SchoolClass.builder().id(1L).name("Primary 5").build();
        Teacher teacherToSave = Teacher.builder().name("Mr. John").staffNumber("TEA123").build();
        Teacher savedTeacher = Teacher.builder().id(1L).name("Mr. John").staffNumber("TEA123").build();

        Subject subjectToSave = Subject.builder().name("Mathematics").schoolClass(schoolClass).teacher(savedTeacher).build();
        Subject savedSubject = Subject.builder().id(1L).name("Mathematics").schoolClass(schoolClass).teacher(savedTeacher).build();

        // Simulates that a school class with id 1L exists in the database
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

        // Simulates that no duplicate teacher exists in the DB (i.e. we can proceed to save).
        when(teacherRepository.findByNameIgnoreCaseAndStaffNumber("Mr. John", "TEA123")).thenReturn(Optional.empty());

        // Simulates that the subject name for this class hasn’t already been created.
        when(subjectRepository.findByNameIgnoreCaseAndSchoolClassId("Mathematics", 1L)).thenReturn(Optional.empty());

        // Mocks the save operation — returns a version of the teacher with an ID and subject with an ID.
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);
        when(subjectRepository.save(any(Subject.class))).thenReturn(savedSubject);

        // ACT
        ViewTeacherResponse result = adminService.createTeacher(request);

        // ASSERT
        assertNotNull(result);
        assertEquals("Mr. John", result.getName());
        assertEquals("TEA123", result.getStaffNumber());
        assertEquals("Mathematics", result.getSubjectName());
        assertEquals("Primary 5", result.getClassName());
    }

    @Test
    void createClass_shouldReturnSavedClass_whenNameIsUnique() {
        CreateClassRequest request = CreateClassRequest.builder().name("Primary 2").build();

        when(schoolClassRepository.existsByName(request.getName())).thenReturn(false);

        //when saved in the database, ID is generated...
        SchoolClass savedClass = SchoolClass.builder()
                .id(1L)
                .name("Primary 6")
                .build();

        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(savedClass);

        SchoolClass result = adminService.createClass(request);
        assertNotNull(result);
        assertEquals("Primary 6", result.getName());
        assertEquals(1L, result.getId());

    }

    @Test
    void createClass_shouldThrowException_whenNameAlreadyExists() {
        // Arrange
        CreateClassRequest request = CreateClassRequest.builder().name("Primary 6").build();

        when(schoolClassRepository.existsByName("Primary 6")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminService.createClass(request));

        assertEquals("Class already exists", exception.getMessage());
    }


}
