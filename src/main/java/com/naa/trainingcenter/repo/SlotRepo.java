package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepo extends JpaRepository<Slot, Long> {

    @Query("SELECT s FROM Slot s WHERE simulator_id = :simulatorId")
    List<Slot> getAll(@Param("simulatorId") Long simulatorId) ;

    @Query(value = "SELECT * " +
            "FROM slot sl " +
            "INNER JOIN simulator s ON s.id = sl.simulator_id " +
            "WHERE TIME(NOW()) BETWEEN sl.start_time AND sl.end_time " +
            "GROUP BY s.id", nativeQuery = true)
    List<Slot> getCurrentSlotsWithSimulators();
}
