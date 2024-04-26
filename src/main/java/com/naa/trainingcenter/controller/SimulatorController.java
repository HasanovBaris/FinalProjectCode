package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.domain.entities.Simulator;
import com.naa.trainingcenter.dto.SimulatorDto;
import com.naa.trainingcenter.service.SimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/simulators")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SimulatorController {

    private final SimulatorService simulatorService;

    @GetMapping
    public List<SimulatorDto> getAll() {
        return simulatorService.findAll();
    }

    @GetMapping("/{id}")
    public SimulatorDto getById(@PathVariable("id") Long id) {
        return simulatorService.findById(id);
    }

    @PostMapping
    public Simulator save(@RequestBody SimulatorDto simulatorDto){
        return simulatorService.save(simulatorDto);
    }

    @PutMapping("/{id}")
    public Simulator update(@RequestBody SimulatorDto simulatorDto, @PathVariable("id") Long simulatorId){
        return simulatorService.update(simulatorDto, simulatorId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long simulatorId){
        return simulatorService.delete(simulatorId);
    }

}
