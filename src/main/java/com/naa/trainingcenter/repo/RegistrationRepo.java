package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

    @Query(value = "Select * from registration where planning_id= :planningId", nativeQuery = true)
    List<Registration> findAllByPlanningId(Long planningId);

}
