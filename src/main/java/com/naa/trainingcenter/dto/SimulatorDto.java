package com.naa.trainingcenter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulatorDto {
    Long id;
    String name;
    List<SlotDto> slots;
}
