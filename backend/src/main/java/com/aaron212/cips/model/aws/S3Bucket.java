package com.aaron212.cips.model.aws;

import com.aaron212.cips.model.CloudStorage;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AWS")
public class S3Bucket extends CloudStorage {
    @Override
    public void provision(int capacityGB) {
        System.out.println("AWS: Creating S3 Bucket with unlimited capacity (simulated " + capacityGB + "GB allocation).");
    }

    @Override
    public String uploadFile(String filename) {
        return "s3://my-bucket/" + filename;
    }
}
