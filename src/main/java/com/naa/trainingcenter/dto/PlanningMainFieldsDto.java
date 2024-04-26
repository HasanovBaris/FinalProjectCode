package com.naa.trainingcenter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningMainFieldsDto {
    Long simulatorId;
    Long slotId;
    LocalDate planningDate;
}
