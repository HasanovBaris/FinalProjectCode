package com.naa.trainingcenter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naa.trainingcenter.domain.entities.Simulator;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class CurrentSlotDto {
    Long id;
    String slotName;
    Simulator simulator;
    @JsonFormat(pattern="HH:mm")
    LocalTime startTime;
    @JsonFormat(pattern="HH:mm")
    LocalTime endTime;
}
