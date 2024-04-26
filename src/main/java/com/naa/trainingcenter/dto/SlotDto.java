package com.naa.trainingcenter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naa.trainingcenter.domain.entities.Simulator;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotDto {
    Long id;
    String slotName;
    @JsonFormat(pattern="HH:mm")
    LocalTime startTime;
    @JsonFormat(pattern="HH:mm")
    LocalTime endTime;

}
