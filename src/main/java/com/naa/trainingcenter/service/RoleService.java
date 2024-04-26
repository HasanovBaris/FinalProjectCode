package com.naa.trainingcenter.service;

import com.naa.trainingcenter.domain.entities.Role;
import com.naa.trainingcenter.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo roleRepo;

    public List<Role> getAllRoles(){
        return (List<Role>) roleRepo.findAll();
    }
}
