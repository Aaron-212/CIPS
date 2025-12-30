package com.aaron212.cips.service;

import com.aaron212.cips.model.ComputeInstance;
import com.aaron212.cips.repository.ComputeInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputeInstanceService {
    @Autowired
    private ComputeInstanceRepository computeInstanceRepository;

    public List<ComputeInstance> findAll() {
        return computeInstanceRepository.findAll();
    }
}
