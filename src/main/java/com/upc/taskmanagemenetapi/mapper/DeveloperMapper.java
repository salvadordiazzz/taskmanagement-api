package com.upc.taskmanagemenetapi.mapper;

import com.upc.taskmanagemenetapi.dto.request.DeveloperRequest;
import com.upc.taskmanagemenetapi.dto.response.DeveloperResponse;
import com.upc.taskmanagemenetapi.model.Developer;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapper {
    public Developer toDeveloper(DeveloperRequest developerRequest) {
        Developer developer = new Developer();
        developer.setName(developerRequest.name());
        return developer;
    }
    public DeveloperResponse toDeveloperResponse(Developer developer) {
        return new DeveloperResponse(developer.getId(), developer.getName());
    }
}
