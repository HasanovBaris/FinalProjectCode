package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.CustomerCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCompanyRepo extends JpaRepository <CustomerCompany, Long>{

    List<CustomerCompany> findAllByIsActive(boolean isActive);
}
