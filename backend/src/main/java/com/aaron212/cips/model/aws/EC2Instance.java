package com.aaron212.cips.model.aws;

import com.aaron212.cips.model.ComputeInstance;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AWS")
public class EC2Instance extends ComputeInstance {
    @Override
    public void onStarted() {
        System.out.println("AWS: Starting EC2 t3.micro instance...");
    }

    @Override
    public void onStopped() {
        System.out.println("AWS: Stopping EC2 instance...");
    }
}
