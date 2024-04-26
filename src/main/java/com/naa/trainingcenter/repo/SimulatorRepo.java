package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Simulator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulatorRepo extends JpaRepository<Simulator, Long> {

}