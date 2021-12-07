package com.maiia.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class AvailabilityDto {
    @PositiveOrZero
    private Integer id;

    @NotNull
    @PositiveOrZero
    private Integer practitionerId;

    @NotNull
    //@Future
    private LocalDateTime startDate;

    @NotNull
    //@Future
    private LocalDateTime endDate;
}
