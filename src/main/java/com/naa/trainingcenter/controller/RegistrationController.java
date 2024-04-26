package com.naa.trainingcenter.controller;


import com.naa.trainingcenter.domain.entities.Registration;
import com.naa.trainingcenter.dto.RegistrationDto;
import com.naa.trainingcenter.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public List<RegistrationDto> getAll(){
        return registrationService.findAll();
    }

    @GetMapping("/all/{planningId}")
    public List<RegistrationDto> getAllById(@PathVariable Long planningId){
        return registrationService.findAllById(planningId);
    }

    @GetMapping("/{id}")
    public RegistrationDto getById(@PathVariable("id") Long id){
        return registrationService.findById(id);
    }

    @PostMapping("/save")
    public Registration save(@RequestBody RegistrationDto registrationDto){
        return registrationService.save(registrationDto);
    }

    @PostMapping("/saveAll/{planningId}")
    public Registration saveAll(@RequestBody List<RegistrationDto> registrationDtoList, @PathVariable("planningId") Long planningId, HttpServletRequest request){
        return registrationService.saveAll(registrationDtoList, planningId, request);
    }

    @PutMapping("/{id}")
    public Registration update(@RequestBody RegistrationDto registrationDto, @PathVariable("id") Long registrationId){
        return registrationService.update(registrationDto, registrationId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long registrationId){
        return registrationService.delete(registrationId);
    }
}
