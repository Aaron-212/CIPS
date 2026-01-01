package com.aaron212.cips.controller;

import com.aaron212.cips.factory.AWSFactory;
import com.aaron212.cips.factory.GCPFactory;
import com.aaron212.cips.factory.CloudInfrastructureFactory;
import com.aaron212.cips.model.ComputeInstance;
import com.aaron212.cips.repository.ComputeInstanceRepository;
import com.aaron212.cips.dto.ComputeInstanceSummaryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/computeInstance")
public class ComputeInstanceController {
    private final ComputeInstanceRepository computeInstanceRepository;

    public ComputeInstanceController(ComputeInstanceRepository computeInstanceRepository) {
        this.computeInstanceRepository = computeInstanceRepository;
    }

    @GetMapping("/fetch")
    public List<ComputeInstance> fetchAllComputeInstances() {
        return computeInstanceRepository.findAll();
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Object> fetchComputeInstance(@PathVariable Long id) {
        Optional<ComputeInstance> instance = computeInstanceRepository.findById(id);
        return instance
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/summary")
    public ComputeInstanceSummaryDTO getComputeInstanceSummary() {
        long activeCount = computeInstanceRepository.countByState(ComputeInstance.State.RUNNING);
        long totalCount = computeInstanceRepository.count();
        return new ComputeInstanceSummaryDTO(activeCount, totalCount);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createComputeInstance(@RequestParam String provider) {
        CloudInfrastructureFactory factory;

        if (provider.equalsIgnoreCase("AWS")) {
            factory = new AWSFactory();
        } else if (provider.equalsIgnoreCase("GCP")) {
            factory = new GCPFactory();
        } else {
            return ResponseEntity.badRequest().body("Invalid Provider");
        }

        ComputeInstance computeInstance = factory.createCompute();
        computeInstance.start();
        computeInstanceRepository.save(computeInstance);

        return ResponseEntity.ok(computeInstance.getId());
    }

    @DeleteMapping("/destroy/{id}")
    public ResponseEntity<Object> destroyComputeInstanceById(@PathVariable Long id) {
        if (computeInstanceRepository.findById(id).isPresent()) {
            computeInstanceRepository.deleteById(id);
            return ResponseEntity.ok(id);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cost/{id}")
    public ResponseEntity<Object> costComputeInstanceById(@PathVariable Long id) {
        if (computeInstanceRepository.findById(id).isPresent()) {
            ComputeInstance computeInstance = computeInstanceRepository.findById(id).get();
            BigDecimal cost = computeInstance.getTotalAccruedCost();
            return ResponseEntity.ok(cost);
        }

        return ResponseEntity.notFound().build();
    }
}
