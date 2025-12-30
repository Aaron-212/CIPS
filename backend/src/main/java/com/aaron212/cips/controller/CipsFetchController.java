package com.aaron212.cips.controller;

import com.aaron212.cips.model.ComputeInstance;
import com.aaron212.cips.repository.ComputeInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fetch")
public class CipsFetchController {
    @Autowired
    private ComputeInstanceRepository computeInstanceRepository;

    @GetMapping("/computeInstance")
    public List<ComputeInstance> fetchAllComputeInstances() {
        return computeInstanceRepository.findAll();
    }

    @GetMapping("/computeInstance/{id}")
    public ResponseEntity<Object> fetchComputeInstance(@PathVariable Long id) {
        Optional<ComputeInstance> instance = computeInstanceRepository.findById(id);
        return instance
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }
}
