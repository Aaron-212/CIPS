package com.aaron212.cips.controller;

import com.aaron212.cips.factory.AWSFactory;
import com.aaron212.cips.factory.CloudInfrastructureFactory;
import com.aaron212.cips.model.ComputeInstance;
import com.aaron212.cips.repository.ComputeInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CipsController {
    @Autowired
    private ComputeInstanceRepository computeInstanceRepository;

    @GetMapping("/fetch/computeInstance")
    public List<ComputeInstance> fetchAllComputeInstances() {
        return computeInstanceRepository.findAll();
    }

    @PostMapping("/create/computeInstance")
    public ResponseEntity<Object> createComputeInstance(@RequestParam String provider) {
        CloudInfrastructureFactory factory;

        if (provider.equalsIgnoreCase("AWS")) {
            factory = new AWSFactory();
        } else {
            return ResponseEntity.badRequest().body("Invalid Provider");
        }

        ComputeInstance computeInstance = factory.createCompute();
        computeInstanceRepository.save(computeInstance);

        return ResponseEntity.ok(computeInstance);
    }
}
