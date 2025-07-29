package com.chidimma.school_management_platform.repository;

import com.chidimma.school_management_platform.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>{
    Optional<Teacher> findById(Long id);
    Optional<Teacher> findByNameIgnoreCaseAndStaffNumber(String name, String staffNumber);
    Optional<Teacher> findBySubjectId(Long subjectId);
}
