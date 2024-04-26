package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.Slot;
import com.naa.trainingcenter.dto.CurrentSlotDto;
import com.naa.trainingcenter.dto.SlotDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.SlotRepo;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {
    private final SlotRepo slotsRepo;
    private final LogService logService;

    public List<SlotDto> findAll() {
        return Mapper.mapAll(slotsRepo.findAll(), SlotDto.class);
    }

    public SlotDto findById(Long id) {
        Slot s = slotsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        return Mapper.map(s, SlotDto.class);
    }

    public List<SlotDto> findAllBySimulatorId(Long simulatorId) {
       return Mapper.mapAll(slotsRepo.getAll(simulatorId), SlotDto.class);
    }

    public Slot save(SlotDto slotDto) {
        return Mapper.map(slotDto, Slot.class);
    }

    public Slot update(SlotDto slotDto, Long slotId) {
        Slot updated = slotsRepo.findById(slotId).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (slotDto != null){
            logService.saveLog("UPDATE", this.getClass().getName(), String.format("Slot %s in simulator %s updated from (%s - %s) to (%s - %s)", updated.getSlotName(),
                updated.getSimulator().getName(), updated.getStartTime(), updated.getEndTime(), slotDto.getStartTime(), slotDto.getEndTime()), username);
            updated.setStartTime(slotDto.getStartTime());
            updated.setEndTime(slotDto.getEndTime());
        }
        return slotsRepo.save(updated);
    }

    public String delete(Long slotId) {
        slotsRepo.deleteById(slotId);
        return "Slot DELETED Successfully!";
    }

    public List<CurrentSlotDto> getCurrentSlotsWithSimulator(){
        return Mapper.mapAll(slotsRepo.getCurrentSlotsWithSimulators(), CurrentSlotDto.class);
    }
}
