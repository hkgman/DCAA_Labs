package com.example.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrisonerReportDto {
    private String name;
    private LocalDate receiptDate;
    private LocalDate endDate;
    private String status;
}
