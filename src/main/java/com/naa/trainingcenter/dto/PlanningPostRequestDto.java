package com.naa.trainingcenter.dto;

import com.naa.trainingcenter.domain.enums.Check_Type;
import com.naa.trainingcenter.domain.enums.PlanningStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningPostRequestDto {
    Long id;
    LocalDate trainingDate;
    Long companyId;
    Long simulatorId;
    Long slotId;
    Long userId;
    Boolean isRegistered;
    Boolean reserved;
    PlanningStatus status;
    Check_Type checkType;
    String technicalDescription;
    String description;
    Integer durationMinutes;
}
