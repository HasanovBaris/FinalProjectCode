package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanningRepo extends JpaRepository<Planning, Long> {
    @Query("SELECT s FROM  Planning s WHERE trainingDate = :date")
    List<Planning> findByDate(@Param("date") LocalDate trainingDate);

    @Transactional
    @Query(value = "SELECT * FROM  Planning WHERE reserved = 0 and training_date = :date and simulator_id = :simId", nativeQuery = true)
    List<Planning> findSoldOrTechnicalsByDateAndBySimulator(@Param("date") LocalDate trainingDate, @Param("simId") Long simId);

    @Transactional
    @Query(value = "SELECT * FROM Planning WHERE reserved = 0 AND simulator_id = :simId AND training_date >= :first AND training_date <= :last", nativeQuery = true)
    List<Planning> findSoldOrTechnicalsByMonthAndBySimulator(@Param("first") LocalDate firstDay,
                                                             @Param("last") LocalDate lastDay,
                                                             @Param("simId") Long simId);

    @Query("SELECT count(*) FROM  Planning WHERE training_date = :date and slot_id =:slotId")
    Integer check(@Param("date") LocalDate trainingDate, @Param("slotId") Long slotId);

    @Query(value = "SELECT * FROM Planning WHERE (is_registered = true or status = 1 or is_cancelled = true) and simulator_id = :id and  training_date <= :date order by training_date desc limit 15", nativeQuery = true)
    List<Planning> findAllBySimulatorId(Long id, LocalDate date);

    @Query(value = "SELECT * FROM Planning WHERE is_registered = true and simulator_id=:simulatorId and slot_id=:slotId and training_date =:planningDate", nativeQuery = true)
    Planning findByMainFields(Long simulatorId, Long slotId, LocalDate planningDate);

    @Query(value = "SELECT * FROM Planning WHERE simulator_id=:simulatorId and slot_id=:slotId and training_date =:planningDate", nativeQuery = true)
    Planning findByMainFieldsForRegistrationEdit(Long simulatorId, Long slotId, LocalDate planningDate);

    @Query(value = "SELECT p.* " +
            "FROM planning p " +
            "JOIN slot s ON p.slot_id = s.id " +
            "WHERE p.training_date = CURRENT_DATE() " +
            "  AND s.simulator_id = :simId " +
            "  AND CURRENT_TIME() BETWEEN s.start_time AND s.end_time " +
            "  AND p.reserved = 0 " +
            "LIMIT 1", nativeQuery = true)
    Optional<Planning> findCurrentPlanningBySimulator(@Param("simId") Long simId);


    @Query(value = "SELECT distinct p.* FROM Planning AS p " +
            "LEFT JOIN registration AS r ON r.planning_id = p.id " +
            "WHERE p.is_registered = true " +
            "AND p.training_date >= :startDate AND p.training_date <= :endDate " +
            "AND p.simulator_id IN (:simulatorIds) " +
            "AND ((:companyId != 0 AND p.company_id = :companyId) OR (:companyId = 0 AND p.company_id IS NOT NULL)) " +
            "AND ((COALESCE(:positionIds, NULL) IS NULL) OR (r.position IN (:positionIds) AND r.position IS NOT NULL)) " +
            "order by p.training_date asc, p.simulator_id asc, p.company_id asc;", nativeQuery = true)
    List<Planning> findByFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("simulatorIds") List<Long> simulatorIds,
            @Param("companyId") Long companyId,
            @Param("positionIds") List<Long> positionIds);


    @Query(value = "SELECT MIN(p.training_date) FROM planning p", nativeQuery = true)
    LocalDate findMinimumDate();

    @Query(value = "SELECT * FROM planning WHERE training_date >= :date AND training_date < DATE_ADD(:date, INTERVAL :count DAY);", nativeQuery = true)
    List<Planning> findAllByCountAndSelectedDay(@Param("date") LocalDate date, @Param("count") Integer count);



}