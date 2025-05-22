package com.upc.taskmanagemenetapi.service;

import com.upc.taskmanagemenetapi.dto.request.DeveloperRequest;
import com.upc.taskmanagemenetapi.dto.response.DeveloperResponse;
import com.upc.taskmanagemenetapi.exception.DuplicateResurceException;
import com.upc.taskmanagemenetapi.exception.ResourceNotFoundException;
import com.upc.taskmanagemenetapi.mapper.DeveloperMapper;
import com.upc.taskmanagemenetapi.model.Developer;
import com.upc.taskmanagemenetapi.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperService {


    private final DeveloperRepository developerRepository;
    private final DeveloperMapper developerMapper;

    @Transactional
    public DeveloperResponse createDeveloper(DeveloperRequest developerRequest) {
        if (developerRepository.existsByName(developerRequest.name())) {
            throw new IllegalArgumentException("Developer already exists");
        }
        Developer saved = developerRepository.save(developerMapper.toDeveloper(developerRequest));
        return developerMapper.toDeveloperResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DeveloperResponse> listAll() {
        return developerRepository.findAll().stream()
                .map(developerMapper::toDeveloperResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DeveloperResponse> findPaginated(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        return developerRepository.findAll(pageable)
                .map(developerMapper::toDeveloperResponse);
    }
    @Transactional(readOnly = true)
    public DeveloperResponse findById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
        return developerMapper.toDeveloperResponse(developer);
    }
    @Transactional
    public DeveloperResponse updateDeveloper(Long id, DeveloperRequest developerRequest) {
        Developer dev=developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
        if (!dev.getName().equals(developerRequest.name()) && developerRepository.existsByName(developerRequest.name())) {
            throw new DuplicateResurceException("Developer already exists");
        }
        dev.setName(developerRequest.name());
        return developerMapper.toDeveloperResponse(developerRepository.save(dev));

    }
    @Transactional
    public void delete(Long id){
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
        developerRepository.delete(developer);
    }

}
