package com.example.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Prisoner, UUID> {

    @Query("SELECT p.name, p.receiptDate, p.term " +
            "FROM Prisoner p")
    List<Object[]> findPrisonerData();

}