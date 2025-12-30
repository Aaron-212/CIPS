package com.aaron212.cips.repository;

import com.aaron212.cips.model.ComputeInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComputeInstanceRepository extends JpaRepository<ComputeInstance, Long> {
}
