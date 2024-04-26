package com.naa.trainingcenter.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naa.trainingcenter.domain.enums.Check_Type;
import com.naa.trainingcenter.domain.enums.PlanningStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate trainingDate;
    @OneToOne
    CustomerCompany company;
    @OneToOne
    Simulator simulator;
    @OneToOne
    Slot slot;
    @OneToOne
    User user;
    @Column
    Integer durationMinutes;
    @Column
    String description;
    Boolean isRegistered;
    Boolean reserved;
    PlanningStatus status;
    String technicalDescription;
    Check_Type checkType;
    @OneToMany(mappedBy = "planning", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Registration> registrations;
    @Column
    Boolean isCancelled;
}
