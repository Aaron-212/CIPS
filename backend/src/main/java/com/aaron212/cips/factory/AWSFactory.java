package com.aaron212.cips.factory;

import com.aaron212.cips.model.*;
import com.aaron212.cips.model.aws.*;

import java.util.Random;

public class AWSFactory implements CloudInfrastructureFactory {
    @Override
    public ComputeInstance createCompute() {
        EC2Instance instance = new EC2Instance();
        instance.setVCpu(2);
        instance.setVRam(4096);
        instance.setVDisk(40960);
        instance.setVBandwidth(1000);
        instance.setVQuota(1000000);
        instance.setDnsName(dnsNameMocker());
        instance.setModelName("m8g.medium");
        return instance;
    }

    @Override
    public CloudStorage createStorage() {
        S3Bucket bucket= new S3Bucket();
        bucket.setName(bucketNameMocker());
        return bucket;
    }

    static private String dnsNameMocker() {
        Random rand = new Random();
        return "ec2-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                "-" + rand.nextInt(256) +
                ".compute-" + rand.nextInt(10) + ".exampleamazonaws.com";
    }

    static private String bucketNameMocker() {
        Random rand = new Random();
        String suffix = Long.toHexString(rand.nextLong() & Long.MAX_VALUE);
        return "s3bucket-" + suffix;
    }
}
