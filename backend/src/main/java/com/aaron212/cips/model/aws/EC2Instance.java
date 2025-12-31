package com.aaron212.cips.model.aws;

import com.aaron212.cips.model.ComputeInstance;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;

@Entity
@DiscriminatorValue("AWS")
@Slf4j
public class EC2Instance extends ComputeInstance {
    @Override
    public void onStarted() {
        log.info("AWS: Starting EC2 {} instance...", this.getModelName());
    }

    @Override
    public void onStopped() {
        log.info("AWS: Stopping EC2 instance...");
    }
}
