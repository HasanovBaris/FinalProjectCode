package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.Simulator;
import com.naa.trainingcenter.dto.SimulatorDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.SimulatorRepo;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class SimulatorService {

    private final SimulatorRepo simulatorRepo;

    public List<SimulatorDto> findAll() {
        return Mapper.mapAll(simulatorRepo.findAll(), SimulatorDto.class);
    }

    public SimulatorDto findById(Long simulatorId){
        return Mapper.map(simulatorRepo.findById(simulatorId).orElseThrow(() -> new ResourceNotFoundException("Simulator not found")), SimulatorDto.class);
    }

    public Simulator save(SimulatorDto simulatorDto) {
        return simulatorRepo.save(Mapper.map(simulatorDto, Simulator.class));
    }

    public Simulator update(SimulatorDto simulatorDto, Long simulatorId) {
        Simulator updated = simulatorRepo.findById(simulatorId).orElseThrow(() -> new ResourceNotFoundException("Simulator not found"));
        if (simulatorDto != null){
            updated.setName(simulatorDto.getName());
        }
        return simulatorRepo.save(updated);
    }

    public String delete(Long simulatorId) {
        simulatorRepo.deleteById(simulatorId);
        return "Simulator DELETED Successfully!";
    }

}
