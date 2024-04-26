package com.naa.trainingcenter.service;


import com.naa.trainingcenter.domain.entities.*;
import com.naa.trainingcenter.dto.PlanningDto;
import com.naa.trainingcenter.dto.RegistrationDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.*;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepo registrationRepo;
    private final PlanningRepo planningRepo;
    private final PlanningService planningService;
    private  final UserRepo userRepo;
    private final LogService logService;

    public List<RegistrationDto> findAll() {
        return Mapper.mapAll(registrationRepo.findAll(), RegistrationDto.class);
    }

    public List<RegistrationDto> findAllById(Long planningId) {
        List<Registration> a = registrationRepo.findAllByPlanningId(planningId);
        List<RegistrationDto> registrationDtoList = Mapper.mapAll(a, RegistrationDto.class);
        for (RegistrationDto r:registrationDtoList) r.setPlanningId(planningId);
        return registrationDtoList;
    }

    public RegistrationDto findById(Long id) {
        return Mapper.map(registrationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Registration not found")), RegistrationDto.class);
    }

    public Registration save(RegistrationDto registrationDto) {
        Planning planning = planningRepo.findById(registrationDto.getPlanningId()).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        Registration registration = Mapper.map(registrationDto, Registration.class);
        registration.setPlanning(planning);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("SAVE", this.getClass().getName(), String.format("Registration saved for planning - %s, %s, %s", planning.getSimulator().getName(), planning.getTrainingDate(), planning.getSlot().getSlotName()), username);
        return registrationRepo.save(registration);
    }

    public Registration saveAll(List<RegistrationDto> registrationDtoList, Long planningId, HttpServletRequest request) {
        Planning planning = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        PlanningDto planningDto = Mapper.map(planning, PlanningDto.class);
        if(registrationDtoList.size() == 0){
            planningDto.setIsRegistered(false);
            planningService.update(planningDto, planningId);
        }
        else{
            for (RegistrationDto registrationDto:registrationDtoList) {
                save(registrationDto);
            }
            planningDto.setIsRegistered(true);
            planningService.update(planningDto, planningId);
        }
        return new Registration();
    }

    public Registration update(RegistrationDto registrationDto, Long registrationId) {
        Registration updated = registrationRepo.findById(registrationId).orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        if (registrationDto != null){
                updated.setName(registrationDto.getName());
                updated.setSurname(registrationDto.getSurname());
                updated.setArrivalTime(registrationDto.getArrivalTime());
                updated.setDepartureTime(registrationDto.getDepartureTime());
                updated.setPosition(registrationDto.getPosition());
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                logService.saveLog("UPDATE", this.getClass().getName(), String.format("Registration - %s, %s, %s updated", updated.getPlanning().getSimulator().getName(), updated.getPlanning().getTrainingDate(), updated.getPlanning().getSlot().getSlotName()), username);
        }
        return registrationRepo.save(updated);
    }

    public String delete(Long registrationId) {
        Registration regDto = registrationRepo.findById(registrationId).orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        Planning p = regDto.getPlanning();
        if(registrationRepo.findAllByPlanningId(p.getId()).size() == 1) {
            p.setIsRegistered(false);
            planningRepo.save(p);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("DELETE", this.getClass().getName(), String.format("Registration - %s, %s, %s deleted", p.getSimulator().getName(), p.getTrainingDate(), p.getSlot().getSlotName()), username);

        registrationRepo.deleteById(registrationId);
        return "Registration DELETED Successfully!";
    }

}
