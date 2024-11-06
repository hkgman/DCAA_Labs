package com.example.report;

import com.example.report.Dto.PrisonerReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;


    @Transactional
    public List<PrisonerReportDto> getPrisoners() {
        List<Object[]> results = reportRepository.findPrisonerData();
        return results.stream()
                .map(result -> {
                    String name = (String) result[0];
                    LocalDate receiptDate = (LocalDate) result[1];
                    int term = (int) result[2];
                    LocalDate endDate = receiptDate.plusYears(term);
                    String status = endDate.isAfter(LocalDate.now()) ? "В заключении" : "Отбыто";
                    return new PrisonerReportDto(name, receiptDate, endDate, status);
                })
                .collect(Collectors.toList());
    }
}
