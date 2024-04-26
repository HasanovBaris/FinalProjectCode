package com.naa.trainingcenter.controller;

import com.naa.trainingcenter.domain.entities.CustomerCompany;
import com.naa.trainingcenter.dto.CustomerCompanyDto;
import com.naa.trainingcenter.service.CustomerCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerCompanyController {

    private final CustomerCompanyService customerCompanyService;

    @GetMapping
    public List<CustomerCompanyDto> getAllActive(){
        return customerCompanyService.findAllActive();
    }

    @GetMapping("/passive")
    public List<CustomerCompanyDto> getAllPassive(){
        return customerCompanyService.findAllPassive();
    }

    @PostMapping
    public CustomerCompany save(@RequestBody CustomerCompanyDto customerCompanyDto){
        return customerCompanyService.save(customerCompanyDto);
    }

    @PutMapping("/{id}")
    public CustomerCompany update(@RequestBody CustomerCompanyDto customerCompanyDto, @PathVariable("id") Long id){
        return customerCompanyService.update(customerCompanyDto, id);
    }

    @PutMapping("/activate/{companyId}/{activation}")
    public CustomerCompany enableOrDisable(@PathVariable("companyId") Long companyId, @PathVariable("activation") boolean activation){
        return customerCompanyService.enableOrDisable(companyId, activation);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id){
        return customerCompanyService.delete(id);
    }
}
