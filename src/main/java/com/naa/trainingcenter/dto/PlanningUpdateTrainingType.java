package com.naa.trainingcenter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningUpdateTrainingType {
    ArrayList<Long> trainingTypeList;
}