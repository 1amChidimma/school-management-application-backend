package com.chidimma.school_management_platform.repository;

import com.chidimma.school_management_platform.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySchoolClassId(Long classId);
    Optional<Subject> findByNameIgnoreCaseAndSchoolClassId(String name, Long classId);
}
