package com.naa.trainingcenter.dto;


import com.naa.trainingcenter.domain.entities.*;
import com.naa.trainingcenter.domain.enums.Check_Type;
import com.naa.trainingcenter.domain.enums.PlanningStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PlanningDto {
    Long id;
    LocalDate trainingDate;
    CustomerCompany company;
    Simulator simulator;
    Slot slot;
    User user;
    Boolean isRegistered;
    Boolean reserved;
    PlanningStatus status;
    String technicalDescription;
    Check_Type checkType;
    List<Registration> registrations;
    String description;
    Integer durationMinutes;
    Boolean isCancelled;
    Boolean feedbackGave;
}
