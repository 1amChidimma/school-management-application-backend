package com.chidimma.school_management_platform.repository;

import com.chidimma.school_management_platform.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long>{

    Optional<SchoolClass> findById(Long id);

    boolean existsByName(String name);
}
