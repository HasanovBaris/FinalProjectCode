package com.naa.trainingcenter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCompanyDto {
    Long id;
    String name;
    Boolean isActive;
}
