package com.aaron212.cips.controller;

import com.aaron212.cips.factory.AWSFactory;
import com.aaron212.cips.factory.CloudInfrastructureFactory;
import com.aaron212.cips.model.ComputeInstance;
import com.aaron212.cips.repository.ComputeInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/create")
public class CipsCreateController {
    @Autowired
    private ComputeInstanceRepository computeInstanceRepository;

    @PostMapping("/computeInstance")
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
