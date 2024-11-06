package com.example.LabP2.repository;

import com.example.LabP2.models.Prisoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrisonerRepository extends JpaRepository<Prisoner, UUID> {

}
