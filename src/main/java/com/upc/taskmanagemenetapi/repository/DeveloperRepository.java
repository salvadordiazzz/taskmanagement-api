package com.upc.taskmanagemenetapi.repository;

import com.upc.taskmanagemenetapi.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByName(String name);
    boolean existsByName(String name);
    Optional<Developer> findById(Integer id);
    Page<Developer> findAll (Pageable pageable);
}
