package com.upc.taskmanagemenetapi.controller;

import com.upc.taskmanagemenetapi.dto.request.TaskRequest;
import com.upc.taskmanagemenetapi.dto.response.TaskResponse;
import com.upc.taskmanagemenetapi.model.Task;
import com.upc.taskmanagemenetapi.model.enums.TaskStatus;
import com.upc.taskmanagemenetapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse task = taskService.create(request);
        return ResponseEntity.ok(task);
    }
    @GetMapping("/paginado")
    public ResponseEntity<Page<TaskResponse>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.findAll(page, size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }
    @GetMapping("/date-range")
    public ResponseEntity<Page<TaskResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.findByDateRange(startDate, endDate, page, size));
    }
    @GetMapping("/developer/{developerId}/active")
    public ResponseEntity<List<TaskResponse>> getByDeveloperActives(
            @PathVariable Long developerId){
        List<TaskResponse> tasks = taskService.findActiveTasksByDeveloperId(developerId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        TaskResponse task = taskService.update(id, request);
        return ResponseEntity.ok(task);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        TaskResponse task = taskService.updateStatus(id, status);
        return ResponseEntity.ok(task);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
