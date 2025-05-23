package com.upc.taskmanagemenetapi.mapper;

import com.upc.taskmanagemenetapi.dto.request.TaskRequest;
import com.upc.taskmanagemenetapi.dto.response.TaskResponse;
import com.upc.taskmanagemenetapi.model.Developer;
import com.upc.taskmanagemenetapi.model.Task;
import com.upc.taskmanagemenetapi.model.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getDeveloper().getName(),
                task.getStartDate(),
                task.getEndDate()

        );
    }
    public Task toEntity(TaskRequest taskRequest, Developer developer) {
        return Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .developer(developer)
                .status(TaskStatus.PENDING)
                .startDate(taskRequest.startDate())
                .endDate(taskRequest.endDate())
                .build();
    }
}
