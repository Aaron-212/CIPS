package com.aaron212.cips.model.aws;

import com.aaron212.cips.model.CloudStorage;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AWS")
public class S3Bucket extends CloudStorage {
    @Override
    public String uploadFile(String filename) {
        return "s3://my-bucket/" + filename;
    }
}
