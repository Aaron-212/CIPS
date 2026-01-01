package com.aaron212.cips.controller;

import com.aaron212.cips.factory.AWSFactory;
import com.aaron212.cips.factory.GCPFactory;
import com.aaron212.cips.factory.CloudInfrastructureFactory;
import com.aaron212.cips.model.CloudStorage;
import com.aaron212.cips.repository.CloudStorageRepository;
import com.aaron212.cips.dto.CloudStorageSummaryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cloudStorage")
public class CloudStorageController {
    private final CloudStorageRepository cloudStorageRepository;

    public CloudStorageController(CloudStorageRepository cloudStorageRepository) {
        this.cloudStorageRepository = cloudStorageRepository;
    }

    @GetMapping("/fetch")
    public List<CloudStorage> fetchAllCloudStorages() {
        return cloudStorageRepository.findAll();
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Object> fetchCloudStorage(@PathVariable Long id) {
        Optional<CloudStorage> storage = cloudStorageRepository.findById(id);
        return storage
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/summary")
    public CloudStorageSummaryDTO getCloudStorageSummary() {
        long count = cloudStorageRepository.count();
        Long totalSize = cloudStorageRepository.sumSize();
        return new CloudStorageSummaryDTO(count, totalSize != null ? totalSize : 0L);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCloudStorage(@RequestParam String provider) {
        CloudInfrastructureFactory factory;

        if (provider.equalsIgnoreCase("AWS")) {
            factory = new AWSFactory();
        } else if (provider.equalsIgnoreCase("GCP")) {
            factory = new GCPFactory();
        } else {
            return ResponseEntity.badRequest().body("Invalid Provider");
        }

        CloudStorage cloudStorage = factory.createStorage();
        cloudStorageRepository.save(cloudStorage);

        return ResponseEntity.ok(cloudStorage.getId());
    }

    @DeleteMapping("/destroy/{id}")
    public ResponseEntity<Object> destroyCloudStorageById(@PathVariable Long id) {
        if (cloudStorageRepository.findById(id).isPresent()) {
            cloudStorageRepository.deleteById(id);
            return ResponseEntity.ok(id);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cost/{id}")
    public ResponseEntity<Object> costCloudStorageById(@PathVariable Long id) {
        if (cloudStorageRepository.findById(id).isPresent()) {
            CloudStorage cloudStorage = cloudStorageRepository.findById(id).get();
            BigDecimal cost = cloudStorage.getTotalAccruedCost();
            return ResponseEntity.ok(cost);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/increase/{id}")
    public ResponseEntity<Object> upgradeCloudStorage(@PathVariable Long id, @RequestParam Integer size) {
        if (cloudStorageRepository.findById(id).isPresent()) {
            CloudStorage cloudStorage = cloudStorageRepository.findById(id).get();
            cloudStorage.increaseSize(size);
            cloudStorageRepository.save(cloudStorage);
            return ResponseEntity.ok(id);
        }

        return ResponseEntity.notFound().build();
    }
}
