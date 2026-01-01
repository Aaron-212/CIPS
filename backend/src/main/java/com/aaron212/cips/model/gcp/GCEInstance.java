package com.aaron212.cips.model.gcp;

import com.aaron212.cips.model.ComputeInstance;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;

@Entity
@DiscriminatorValue("GCP")
@Slf4j
public class GCEInstance extends ComputeInstance {
    @Override
    public void onStarted() {
        log.info("GCP: Starting GCE {} instance...", this.getModelName());
    }

    @Override
    public void onStopped() {
        log.info("GCP: Stopping GCE instance...");
    }
}

