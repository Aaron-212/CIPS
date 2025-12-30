package com.aaron212.cips.factory;

import com.aaron212.cips.model.*;

public interface CloudInfrastructureFactory {
    ComputeInstance createCompute();

    CloudStorage createStorage();
}
