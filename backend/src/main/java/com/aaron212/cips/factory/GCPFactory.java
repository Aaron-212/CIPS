package com.aaron212.cips.factory;

import com.aaron212.cips.model.*;
import com.aaron212.cips.model.gcp.*;

import java.util.Random;

public class GCPFactory implements CloudInfrastructureFactory {
    @Override
    public ComputeInstance createCompute() {
        GCEInstance instance = new GCEInstance();
        instance.setVCpu(2);
        instance.setVRam(4096);
        instance.setVDisk(40960);
        instance.setVBandwidth(2000);
        instance.setVQuota(2000000);
        instance.setDnsName(dnsNameMocker());
        instance.setModelName("n2-standard-2");
        return instance;
    }

    @Override
    public CloudStorage createStorage() {
        return new GCSBucket();
    }

    static private String dnsNameMocker() {
        Random rand = new Random();
        return "gce-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                ".c.example-googlecloudplatform.internal";
    }
}
