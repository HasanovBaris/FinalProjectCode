package com.naa.trainingcenter.dto;

import com.naa.trainingcenter.domain.entities.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    String surname;
    String username;
    String position;
    Set<Role> roles;
}
