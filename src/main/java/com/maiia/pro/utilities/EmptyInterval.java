package com.maiia.pro.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class EmptyInterval {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
