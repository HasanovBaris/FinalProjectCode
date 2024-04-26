package com.naa.trainingcenter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
    Long id;
    LocalDateTime timestamp;
    String level;
    String logger;
    String message;
    String username;
}
