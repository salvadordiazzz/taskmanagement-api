package com.upc.taskmanagemenetapi.service;

import com.upc.taskmanagemenetapi.dto.request.TaskRequest;
import com.upc.taskmanagemenetapi.dto.response.TaskResponse;
import com.upc.taskmanagemenetapi.exception.BusinessRuleException;
import com.upc.taskmanagemenetapi.exception.DuplicateResurceException;
import com.upc.taskmanagemenetapi.exception.ResourceNotFoundException;
import com.upc.taskmanagemenetapi.mapper.TaskMapper;
import com.upc.taskmanagemenetapi.model.Developer;
import com.upc.taskmanagemenetapi.model.Task;
import com.upc.taskmanagemenetapi.model.enums.TaskStatus;
import com.upc.taskmanagemenetapi.repository.DeveloperRepository;
import com.upc.taskmanagemenetapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final DeveloperRepository developerRepository;

    private static final int MAX_ACTIVE_TASKS = 5;

    @Transactional
    public TaskResponse create(TaskRequest taskRequest) {

        if (taskRepository.existsByTitle(taskRequest.title())){
            throw new DuplicateResurceException("Task with this title already exists");
        }

        Developer developer = developerRepository.findById(taskRequest.developerId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        int activeTaskCount = taskRepository.countActiveTasksByDeveloperId(taskRequest.developerId(), List.of(TaskStatus.PENDING, TaskStatus.IN_PROGRESS));

        if (activeTaskCount >= MAX_ACTIVE_TASKS) {
            throw new BusinessRuleException("Developer has reached the maximum number of active tasks");
        }
        Task savedTask = taskRepository.save(taskMapper.toEntity(taskRequest,developer));
        return taskMapper.toResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findAll(int page, int size) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(page, size));
        return tasks.map(taskMapper::toResponse);
    }
    @Transactional(readOnly = true)
    public TaskResponse findById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return taskMapper.toResponse(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException("Start date cannot be after end date");
        }
        Page<Task> tasks = taskRepository.findTaskByDateRange(startDate, endDate, PageRequest.of(page, size));
        return tasks.map(taskMapper::toResponse);
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> findActiveTasksByDeveloperId(long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
        List<Task> tasks = taskRepository.findTasksByDeveloperIdAndStatus(developer.getId(), List.of( TaskStatus.PENDING, TaskStatus.IN_PROGRESS));
        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse update(long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskRepository.existsByTitle(taskRequest.title()) && !taskRequest.title().equals(task.getTitle())) {
            throw new DuplicateResurceException("Task with this title already exists");
        }
        if (task.getStatus() != TaskStatus.PENDING) {
            throw new BusinessRuleException("Task cannot be updated because it is not in PENDING status");
        }

        Developer developer = developerRepository.findById(task.getDeveloper().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        if (!task.getDeveloper().getId().equals(developer.getId())) {
            int activeTaskCount = taskRepository.countActiveTasksByDeveloperId(developer.getId(), List.of(TaskStatus.PENDING, TaskStatus.IN_PROGRESS));
            if (activeTaskCount >= MAX_ACTIVE_TASKS) {
                throw new BusinessRuleException("Developer has reached the maximum number of active tasks");
            }
            task.setDeveloper(developer);
        }
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }
    @Transactional
    public TaskResponse updateStatus(long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (task.getStatus()!=TaskStatus.PENDING && status==TaskStatus.IN_PROGRESS) {
            throw new BusinessRuleException("Task cannot be updated because it is not in PENDING status");
        }
        if (task.getStatus() != TaskStatus.IN_PROGRESS && status==TaskStatus.COMPLETED) {
            throw new BusinessRuleException("Task cannot be updated because it is not in IN_PROGRESS status");
        }
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }
    @Transactional
    public void delete(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

}
