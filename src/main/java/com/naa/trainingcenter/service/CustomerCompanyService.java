package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.CustomerCompany;
import com.naa.trainingcenter.dto.CustomerCompanyDto;
import com.naa.trainingcenter.exception.ResourceNotFoundException;
import com.naa.trainingcenter.repo.CustomerCompanyRepo;
import com.naa.trainingcenter.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCompanyService {

    private final CustomerCompanyRepo customerCompanyRepo;
    private final LogService logService;

    public List<CustomerCompanyDto> findAllActive() {
        List<CustomerCompany> companies = customerCompanyRepo.findAllByIsActive(true);
        companies.sort(Comparator.comparing(CustomerCompany::getName));
        return Mapper.mapAll(companies ,CustomerCompanyDto.class);
    }

    public List<CustomerCompanyDto> findAllPassive() {
        return Mapper.mapAll(customerCompanyRepo.findAllByIsActive(false) ,CustomerCompanyDto.class);
    }

    public CustomerCompany save(CustomerCompanyDto customerCompanyDto) {
        customerCompanyDto.setIsActive(true);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("SAVE", this.getClass().getName(), String.format("Company with name %s saved", customerCompanyDto.getName()), username);
        return customerCompanyRepo.save(Mapper.map(customerCompanyDto, CustomerCompany.class));
    }

    public CustomerCompany update(CustomerCompanyDto customerCompanyDto, Long id) {
        CustomerCompany customerCompany = customerCompanyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        if (customerCompanyDto != null){
            customerCompany.setName(customerCompanyDto.getName());
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Company with name %s updated", customerCompany.getName()), username);
        return customerCompanyRepo.save(customerCompany);
    }

    public CustomerCompany enableOrDisable(Long companyId, boolean activation){
        CustomerCompany customerCompany = customerCompanyRepo.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        customerCompany.setIsActive(activation);

        String result = activation ? "enabled" : "disabled";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("UPDATE", this.getClass().getName(), String.format("Company with name %s %s", customerCompany.getName(), result), username);

        return customerCompanyRepo.save(customerCompany);
    }

    public String delete(Long id) {
        CustomerCompany customerCompany = customerCompanyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog("DELETE", this.getClass().getName(), String.format("Company with name %s deleted", customerCompany.getName()), username);

        customerCompanyRepo.deleteById(id);
        return "CustomerCompany DELETED Successfully!";
    }
}
