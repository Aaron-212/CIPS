package com.aaron212.cips.model.gcp;

import com.aaron212.cips.model.CloudStorage;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GCP")
public class GCSBucket extends CloudStorage {
    @Override
    public String uploadFile(String filename) {
        return "gs://my-bucket/" + filename;
    }
}

