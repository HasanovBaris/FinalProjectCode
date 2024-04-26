package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.domain.entities.Slot;
import com.naa.trainingcenter.dto.SlotDto;
import com.naa.trainingcenter.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SlotsController {
    private final SlotService slotsService;

    @GetMapping
    public List<SlotDto> getAll(){
        return slotsService.findAll();
    }

    @GetMapping("/getById/{id}")
    public SlotDto getById(@PathVariable("id") Long id){
        return slotsService.findById(id);
    }

    @GetMapping("/{simulator_id}")
    public List<SlotDto> getAllBySimulator(@PathVariable("simulator_id") Long simulatorId){
        return slotsService.findAllBySimulatorId(simulatorId);
    }

    @PostMapping
    public Slot save(@RequestBody SlotDto slotDto) {
        return slotsService.save(slotDto);
    }

    @PutMapping("/{id}")
    public Slot update(@RequestBody SlotDto slotDto, @PathVariable("id") Long slotId){
        return slotsService.update(slotDto, slotId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long slotId){
        return slotsService.delete(slotId);
    }

}
