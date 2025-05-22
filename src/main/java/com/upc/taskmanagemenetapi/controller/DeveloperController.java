package com.upc.taskmanagemenetapi.controller;

import com.upc.taskmanagemenetapi.dto.request.DeveloperRequest;
import com.upc.taskmanagemenetapi.dto.response.DeveloperResponse;
import com.upc.taskmanagemenetapi.mapper.DeveloperMapper;
import com.upc.taskmanagemenetapi.model.Developer;
import com.upc.taskmanagemenetapi.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;
    private final DeveloperMapper developerMapper;


    @PostMapping
    public ResponseEntity<DeveloperResponse> create(@Valid @RequestBody DeveloperRequest developerRequest) {
        DeveloperResponse createdDeveloper = developerService.createDeveloper(developerRequest);
        return ResponseEntity.status(201).body(createdDeveloper);
    }
    @GetMapping
    public ResponseEntity<List<DeveloperResponse>> listAll() {
        List<DeveloperResponse> developers = developerService.listAll();
        return ResponseEntity.ok(developers);
    }
    @GetMapping("/paginated")
    public ResponseEntity<Page<DeveloperResponse>> getPaginated(Pageable pageable) {
        return ResponseEntity.ok(developerService.findPaginated(pageable.getPageNumber(), pageable.getPageSize()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeveloperResponse> getById(@PathVariable Long id) {
        DeveloperResponse developer = developerService.findById(id);
        return ResponseEntity.ok(developer);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DeveloperResponse> update(@PathVariable Long id, @Valid @RequestBody DeveloperRequest developerRequest) {
        DeveloperResponse updatedDeveloper = developerService.updateDeveloper(id, developerRequest);
        return ResponseEntity.ok(updatedDeveloper);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        developerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
