package com.naa.trainingcenter.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import liquibase.repackaged.net.sf.jsqlparser.util.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;
}
