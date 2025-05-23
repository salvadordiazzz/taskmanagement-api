package com.upc.taskmanagemenetapi.dto.request;

import com.upc.taskmanagemenetapi.model.Developer;
import com.upc.taskmanagemenetapi.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "Title is mandatory")
        @Size(min=1, max=100)
        String title,

        @NotBlank(message = "Description is mandatory")
        @Size(min=1, max=500)
        String description,


        @NotNull
        Long developerId,

        LocalDate startDate,
        LocalDate endDate
) {
}
