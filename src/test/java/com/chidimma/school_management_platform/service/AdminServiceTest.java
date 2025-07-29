package com.chidimma.school_management_platform.service;

import com.chidimma.school_management_platform.dto.ViewTeacherResponse;
import com.chidimma.school_management_platform.model.SchoolClass;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
