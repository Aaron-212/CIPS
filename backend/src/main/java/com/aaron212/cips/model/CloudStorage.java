package com.aaron212.cips.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "provider")
public abstract class CloudStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private Integer size = 1000; // current size in MB, default to 1000

    private BigDecimal hourlyCost;

    public final void increaseSize(Integer size) {
        if (size == null || size <= 0) {
            return;
        }

        // Saturating add
        long sum = (long) this.size + (long) size;
        this.size = (int) max((long) Integer.MIN_VALUE, min(sum, (long) Integer.MAX_VALUE));
    }

    public abstract String uploadFile(String filename);

    public final void calculateHourlyCost() {
        BigDecimal diskRate = new BigDecimal("0.000005");
        BigDecimal total = BigDecimal.ZERO;
        if (size != null) total = total.add(diskRate.multiply(new BigDecimal(size)));

        this.hourlyCost = total;
    }

    public final BigDecimal getTotalAccruedCost() {
        if (createdAt == null || hourlyCost == null) return BigDecimal.ZERO;
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        long billableHours = max(1, hours);

        return hourlyCost.multiply(new BigDecimal(billableHours));
    }
}
