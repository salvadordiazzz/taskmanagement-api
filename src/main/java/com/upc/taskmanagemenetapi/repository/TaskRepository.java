package com.upc.taskmanagemenetapi.repository;

import com.upc.taskmanagemenetapi.model.Task;
import com.upc.taskmanagemenetapi.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(long id);
    boolean existsById(long id);

    boolean existsByTitle(String title);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.developer.id = :developerId AND t.status IN :statuses")
    int countActiveTasksByDeveloperId(long developerId, List<TaskStatus> statuses);

    Page<Task> findAll(Pageable pageable);

    @Query ("SELECT t FROM Task t WHERE t.startDate >=:startDate AND t.endDate <= :endDate")
    Page<Task> findTaskByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);


    @Query ("SELECT t FROM Task t WHERE t.developer.id =:developerId and t.status IN :statuses")
    List<Task> findTasksByDeveloperIdAndStatus(long developerId, List<TaskStatus> statuses);
}
