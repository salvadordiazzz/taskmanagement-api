package com.upc.taskmanagemenetapi.dto.response;

import java.time.LocalDate;

public record TaskResponse(
        long id,
        String title,
        String description,
        String status,
        String developerName,
        LocalDate startDate,
        LocalDate endDate
) {
}
