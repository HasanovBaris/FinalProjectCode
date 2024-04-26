package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.*;
import com.naa.trainingcenter.dto.PlanningDto;
import com.naa.trainingcenter.dto.PlanningMainFieldsDto;
import com.naa.trainingcenter.dto.PlanningPostRequestDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.*;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanningService {

    private final PlanningRepo planningRepo;
    private final CustomerCompanyRepo customerCompanyRepo;
    private final SimulatorRepo simulatorRepo;
    private final SlotRepo slotRepo;
    private final UserRepo userRepo;
    private final LogService logService;



    public List<PlanningDto> findAll() {
        List<Planning> data = planningRepo.findAll();
        return Mapper.mapAll(data, PlanningDto.class);
    }


    public List<PlanningDto> findSoldOrTechnicalsByDate(String trainingDate, Long simId) {
        String[] s = trainingDate.split("-");
        LocalDate date = LocalDate.of(Integer.parseInt(s[2]), Integer.parseInt(s[1]), Integer.parseInt(s[0]));
        return Mapper.mapAll(planningRepo.findSoldOrTechnicalsByDateAndBySimulator(date, simId),  PlanningDto.class);
    }

    public List<PlanningDto> findPlanningsByDate(LocalDate date, Long simId) {
        return Mapper.mapAll(planningRepo.findSoldOrTechnicalsByDateAndBySimulator(date, simId),  PlanningDto.class);
    }

    public List<PlanningDto> findPlanningsByMonth(LocalDate first, LocalDate last, Long simulatorId) {

        return Mapper.mapAll(planningRepo.findSoldOrTechnicalsByMonthAndBySimulator(first, last, simulatorId),  PlanningDto.class);
    }

    public PlanningDto findCurrentPlanningBySimulator(Long simId){
        return Mapper.map(planningRepo.findCurrentPlanningBySimulator(simId).orElseThrow(() -> new ResourceNotFoundException("Planning not found")),  PlanningDto.class);
    }

    public PlanningDto findById(Long id) {
        return Mapper.map(planningRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Planning not found")), PlanningDto.class);
    }

    public List<PlanningDto> findAllBySimulatorId(Long id, LocalDate date) {
        List<Planning> planningList = planningRepo.findAllBySimulatorId(id, date);
        return Mapper.mapAll(planningList, PlanningDto.class);
    }

    public Planning save(PlanningPostRequestDto planningPostRequestDto) {
       Integer number = planningRepo.check(planningPostRequestDto.getTrainingDate(), planningPostRequestDto.getSlotId());
       String companyName;
        if(number == 0){
            System.out.println(planningPostRequestDto);
            PlanningDto planningDto = Mapper.map(planningPostRequestDto, PlanningDto.class);
            Simulator sim = simulatorRepo.findById(planningPostRequestDto.getSimulatorId()).orElseThrow(() -> new ResourceNotFoundException("Simulator not found"));
            Slot slot = slotRepo.findById(planningPostRequestDto.getSlotId()).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
            User user = userRepo.findById(planningPostRequestDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            companyName = "Technical Service";
            if (planningPostRequestDto.getCompanyId() != null) {
                CustomerCompany cc = customerCompanyRepo.findById(planningPostRequestDto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
                companyName = cc.getName();
                planningDto.setCompany(cc);
            }
            String ticket = planningPostRequestDto.getReserved() ? "booked" : "sold";
            planningDto.setSimulator(sim);
            planningDto.setSlot(slot);
            planningDto.setUser(user);
            planningDto.setIsCancelled(false);
            Planning planning = Mapper.map(planningDto, Planning.class);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            logService.saveLog("SAVE", this.getClass().getName(), String.format("Planning in %s slot %s  %s for '%s' %s", planningDto.getSimulator().getName(), planningDto.getSlot().getSlotName(), planning.getTrainingDate(), companyName, ticket), username);
            return planningRepo.save(planning);
        }
        else{
            return new Planning();
        }
    }

    public Planning saveAll(List<PlanningPostRequestDto> planningPostRequestDtoList) {
        try {
            for (PlanningPostRequestDto planningPostRequestDto: planningPostRequestDtoList){
                save(planningPostRequestDto);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new Planning();
    }

    public Planning update(PlanningDto planningDto, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
            updated.setCompany(planningDto.getCompany());
            updated.setTrainingDate(planningDto.getTrainingDate());
            updated.setSimulator(planningDto.getSimulator());
            updated.setSlot(planningDto.getSlot());
            updated.setUser(planningDto.getUser());
            updated.setIsRegistered(planningDto.getIsRegistered());
            updated.setStatus(planningDto.getStatus());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s updated",
                    updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

    public Planning updateByAdmin(PlanningPostRequestDto planningPostRequestDto, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        Simulator sim = simulatorRepo.findById(planningPostRequestDto.getSimulatorId()).orElseThrow(() -> new ResourceNotFoundException("Simulator not found"));
        Slot slot = slotRepo.findById(planningPostRequestDto.getSlotId()).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        CustomerCompany cc = customerCompanyRepo.findById(planningPostRequestDto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        updated.setTrainingDate(planningPostRequestDto.getTrainingDate());
        updated.setCompany(cc);
        updated.setSimulator(sim);
        updated.setSlot(slot);
        updated.setCheckType(planningPostRequestDto.getCheckType());
        updated.setDurationMinutes(planningPostRequestDto.getDurationMinutes());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s updated!",
                updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

    public String delete(Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        String ticket = updated.getReserved() ? "booked" : "sold";

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("DELETE", this.getClass().getName(), String.format("Planning (%s) - %s, %s, %s deleted", ticket, updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);

        planningRepo.deleteById(planningId);
        return "Planning DELETED Successfully!";
    }


    public PlanningDto findPlanningByMainFields(PlanningMainFieldsDto planningMainFieldsDto) {
        Planning p = planningRepo.findByMainFields(planningMainFieldsDto.getSimulatorId(),
                planningMainFieldsDto.getSlotId(), planningMainFieldsDto.getPlanningDate());

        return p != null ?  Mapper.map(p, PlanningDto.class) : new PlanningDto();
    }

    public PlanningDto findByMainFieldsForRegistrationEdit(PlanningMainFieldsDto planningMainFieldsDto) {
        Planning p = new Planning();
        if(planningMainFieldsDto.getSimulatorId() == 4L || planningMainFieldsDto.getSimulatorId() == 5L){ // this condition is for boeing simulator what that is one physical simulator
            Planning p4 = planningRepo.findByMainFieldsForRegistrationEdit(4L,
                    planningMainFieldsDto.getSimulatorId() == 4 ? planningMainFieldsDto.getSlotId() : planningMainFieldsDto.getSlotId() - 6,
                    planningMainFieldsDto.getPlanningDate());
            Planning p5 = planningRepo.findByMainFieldsForRegistrationEdit(5L,
                    planningMainFieldsDto.getSimulatorId() == 5 ? planningMainFieldsDto.getSlotId() : planningMainFieldsDto.getSlotId() + 6,
                    planningMainFieldsDto.getPlanningDate());

            if(p4 == null && p5 == null){
                p = null;
            }else{
                if (planningMainFieldsDto.getSimulatorId() == 4L){
                    if(p4 != null) p = p4;
                    else p=p5;
                }
                else if (planningMainFieldsDto.getSimulatorId() == 5L){
                    if(p5 != null) p = p5;
                    else p=p4;
                }
            }
        }else{
            p = planningRepo.findByMainFieldsForRegistrationEdit(planningMainFieldsDto.getSimulatorId(),
                    planningMainFieldsDto.getSlotId(), planningMainFieldsDto.getPlanningDate());
        }
        if(p == null) return new PlanningDto();
        else return Mapper.map(p, PlanningDto.class);
    }

    public Boolean planningExist(Long simId){
        return planningRepo.findCurrentPlanningBySimulator(simId).isPresent();
    }

    public LocalDate getMinimumDate(){
        return planningRepo.findMinimumDate();
    }

    public List<PlanningDto> findByCountAndSelectedDay(LocalDate date, Integer count) {
        List<Planning> data = planningRepo.findAllByCountAndSelectedDay(date, count);
        return Mapper.mapAll(data, PlanningDto.class);
    }

    public void forbid() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("WARNING", this.getClass().getName(), "Tried unauthorized simulator planning action!", username);
    }

    public Planning updateReservedToSale(String sim, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        updated.setReserved(false);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s changed from" +
                " reserved to sold", updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

    public Planning updateIsCancelled(String sim, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        updated.setIsCancelled(true);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s cancelled!", updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

    public Planning updateRestore(String sim, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        updated.setIsCancelled(false);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s restored!", updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

    public Planning againBooking(String sim, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        updated.setReserved(true);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s reserved again!", updated.getSimulator().getName(), updated.getTrainingDate(), updated.getSlot().getSlotName()), username);
        return planningRepo.save(updated);
    }

       public Planning changeSimulator(String sim, Long planningId) {
        Planning updated = planningRepo.findById(planningId).orElseThrow(() -> new ResourceNotFoundException("Planning not found"));
        Simulator simulator = null;
        Slot slot = null;
        if (updated.getSimulator().getId() == 4L) {
            simulator = simulatorRepo.findById(5L).orElseThrow(() -> new ResourceNotFoundException("Simulator not found"));
            slot = slotRepo.findById(updated.getSlot().getId()+6).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        }
        if (updated.getSimulator().getId() == 5L){
            simulator = simulatorRepo.findById(4L).orElseThrow(() -> new ResourceNotFoundException("Simulator not found"));
            slot = slotRepo.findById(updated.getSlot().getId()-6).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        }
        updated.setSimulator(simulator);
        updated.setSlot(slot);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Planning - %s, %s, %s changed to %s!",
                sim, updated.getTrainingDate(), updated.getSlot().getSlotName(), updated.getSimulator().getName()), username);
        return planningRepo.save(updated);
    }

    public List<PlanningDto> findByDate(String trainingDate) {
        String[] s = trainingDate.split("-");
        LocalDate date = LocalDate.of(Integer.parseInt(s[2]), Integer.parseInt(s[1]), Integer.parseInt(s[0]));
        List<PlanningDto> dtoList = Mapper.mapAll(planningRepo.findByDate(date),  PlanningDto.class);
        return dtoList;
    }
}
