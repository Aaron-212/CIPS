package com.aaron212.cips.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "provider")
public abstract class ComputeInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Column(name = "provider", insertable = false, updatable = false)
    @Getter
    private String provider;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Getter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private Integer vCpu; // number of vCPU cores

    @Getter
    @Setter
    private Integer vRam; // ram size in MB

    @Getter
    @Setter
    private Integer vDisk; // disk size in MB

    @Getter
    @Setter
    private Integer vBandwidth; // bandwidth in Mbps

    @Getter
    @Setter
    private Integer vQuota; // network quota in MB

    @Getter
    @Setter
    private String dnsName;

    @Getter
    @Setter
    private String modelName;

    @Getter
    @Setter
    private State state;

    private BigDecimal hourlyCost;

    public final void start() {
        this.setState(State.RUNNING);
        onStarted();
    }

    public final void stop() {
        this.setState(State.STOPPED);
        onStopped();
    }

    public final void calculateHourlyCost() {
        BigDecimal cpuRate = new BigDecimal("0.02");
        BigDecimal ramRate = new BigDecimal("0.00001");
        BigDecimal diskRate = new BigDecimal("0.000005");
        BigDecimal netRate = new BigDecimal("0.0001");

        BigDecimal total = BigDecimal.ZERO;

        if (vCpu != null) total = total.add(cpuRate.multiply(new BigDecimal(vCpu)));
        if (vRam != null) total = total.add(ramRate.multiply(new BigDecimal(vRam)));
        if (vDisk != null) total = total.add(diskRate.multiply(new BigDecimal(vDisk)));
        if (vBandwidth != null) total = total.add(netRate.multiply(new BigDecimal(vBandwidth)));

        this.hourlyCost = total;
    }

    public final BigDecimal getTotalAccruedCost() {
        if (createdAt == null) return BigDecimal.ZERO;
        if (hourlyCost == null) calculateHourlyCost();
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        long billableHours = Math.max(1, hours);

        return hourlyCost.multiply(new BigDecimal(billableHours));
    }

    public abstract void onStarted();

    public abstract void onStopped();

    public enum State {
        RUNNING,
        STOPPED,
    }
}
