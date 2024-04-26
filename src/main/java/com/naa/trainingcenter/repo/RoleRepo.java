package com.naa.trainingcenter.repo;

import com.naa.trainingcenter.domain.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo extends CrudRepository<Role, Long> {
}
