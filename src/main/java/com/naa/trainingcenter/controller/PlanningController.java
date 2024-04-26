package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.domain.entities.Planning;
import com.naa.trainingcenter.dto.PlanningDto;
import com.naa.trainingcenter.dto.PlanningPostRequestDto;
import com.naa.trainingcenter.dto.PlanningUpdateTrainingType;
import com.naa.trainingcenter.service.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planning")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlanningController {

    private final PlanningService planningService;

    @GetMapping
    public List<PlanningDto> getAll(){
        return planningService.findAll();
    }

    @GetMapping("/selected/{date}/{count}")
    public List<PlanningDto> getAllByCountAndSelectedDay(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("count") Integer count){
        return planningService.findByCountAndSelectedDay(date, count);
    }

    @GetMapping("/get/{id}")
    public PlanningDto getById(@PathVariable Long id){
        return planningService.findById(id);
    }

    @GetMapping("/getLasts/{simulatorId}/{date}")
    public List<PlanningDto> getAllBySimulator(@PathVariable Long simulatorId, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date ){
        return planningService.findAllBySimulatorId(simulatorId, date);
    }

    @GetMapping("/{trainingDate}")
    public List<PlanningDto> getByDate(@PathVariable("trainingDate") String trainingDate){
        return planningService.findByDate(trainingDate);
    }

    @PostMapping("/{sim}")
    public Planning save(@PathVariable String sim, @RequestBody PlanningPostRequestDto planningPostRequestDto){
        return planningService.save(planningPostRequestDto);
    }

    @PostMapping("/bulk/{sim}")
    public Planning saveAll(@PathVariable("sim") String sim,@RequestBody List<PlanningPostRequestDto> planningPostRequestDtoList){
        return planningService.saveAll(planningPostRequestDtoList);
    }

    @PutMapping("/{id}")
    public Planning update(@RequestBody PlanningDto planningDto, @PathVariable("id") Long planningId){
        return planningService.update(planningDto, planningId);
    }

    @PutMapping("/admin-edit/{id}")
    public Planning updateByAdmin(@RequestBody PlanningPostRequestDto planningPostRequestDto, @PathVariable("id") Long planningId){
        return planningService.updateByAdmin(planningPostRequestDto, planningId);
    }

    @PutMapping("/reserved-to-sale/{sim}/{id}")
    public Planning updateReservedToSale(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.updateReservedToSale(sim,planningId);
    }

    @PutMapping("/cancel/{sim}/{id}")
    public Planning updateIsCancelled(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.updateIsCancelled(sim,planningId);
    }

    @PutMapping("/again-booking/{sim}/{id}")
    public Planning againBooking(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.againBooking(sim,planningId);
    }

    @PutMapping("/restore/{sim}/{id}")
    public Planning updateRestore(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.updateRestore(sim,planningId);
    }

    @PutMapping("/change-simulator/{sim}/{id}")
    public Planning changeSimulator(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.changeSimulator(sim,planningId);
    }

    @DeleteMapping("/delete/{sim}/{id}")
    public String delete(@PathVariable("sim") String sim, @PathVariable("id") Long planningId){
        return planningService.delete(planningId);
    }

    @PostMapping("/forbidden")
    public void forbid(){
        planningService.forbid();
    }

}
