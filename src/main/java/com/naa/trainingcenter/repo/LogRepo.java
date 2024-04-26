package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {
    List<Log> findAllByTimestampBetween(LocalDateTime timestamp, LocalDateTime timestamp2);
}
