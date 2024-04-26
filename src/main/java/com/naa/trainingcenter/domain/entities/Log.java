package com.naa.trainingcenter.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @Column(nullable = false)
    String level;

    @Column(nullable = false)
    String logger;

    @Column(nullable = false, columnDefinition = "TEXT")
    String message;

    String username;

}
