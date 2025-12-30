package com.aaron212.cips.factory;

import com.aaron212.cips.model.*;
import com.aaron212.cips.model.aws.*;

public class AWSFactory implements CloudInfrastructureFactory {
    @Override
    public ComputeInstance createCompute() {
        return new EC2Instance();
    }
    @Override
    public CloudStorage createStorage() {
        return new S3Bucket();
    }
}
