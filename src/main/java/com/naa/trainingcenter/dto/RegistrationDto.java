package com.naa.trainingcenter.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class RegistrationDto {
    Long id;
    Long planningId;
    String name;
    String surname;
    LocalTime arrivalTime;
    LocalTime departureTime;
    String position;
    Long createdBy;
    Long trainingTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDto that = (RegistrationDto) o;
        return  Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) && Objects.equals(arrivalTime, that.arrivalTime) &&
                Objects.equals(departureTime, that.departureTime) && Objects.equals(position, that.position) &&
                Objects.equals(createdBy, that.createdBy) && Objects.equals(trainingTypeId, that.trainingTypeId);
    }

    @Override
    public String toString() {
        return id + " " + planningId + " " +
                name + " " + surname + " " + arrivalTime + " " + departureTime + " " +
                position + " " + createdBy + " " + trainingTypeId;
    }
}
