package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.Log;
import com.naa.trainingcenter.dto.LogDto;
import com.naa.trainingcenter.repo.LogRepo;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LogRepo logRepo;

    @Transactional
    public void saveLog(String level, String logger, String message, String username) {
        Log log = new Log();
        log.setTimestamp(LocalDateTime.now());
        log.setLevel(level);
        log.setLogger(logger);
        log.setMessage(message);
        log.setUsername(username);
        entityManager.persist(log);
    }

    public List<LogDto> findAll() {
        return Mapper.mapAll(logRepo.findAll(), LogDto.class);
    }

    public List<LogDto> findBetweenDates(LocalDate start, LocalDate end){
        return Mapper.mapAll(logRepo.findAllByTimestampBetween(start.atTime(LocalTime.of(0, 0)), end.atTime(LocalTime.now())), LogDto.class);
    }
}
