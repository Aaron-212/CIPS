package com.aaron212.cips.repository;

import com.aaron212.cips.model.CloudStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CloudStorageRepository extends JpaRepository<CloudStorage, Long>  {
    @Query("SELECT SUM(c.size) FROM CloudStorage c")
    Long sumSize();
}
