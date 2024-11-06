package com.example.LabP2.service;

import com.example.LabP2.dto.PrisonerDto;
import com.example.LabP2.error.exception.PrisonerNotFoundException;
import com.example.LabP2.models.Prisoner;
import com.example.LabP2.repository.PrisonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrisonerService {
    private final PrisonerRepository prisonerRepository;

    @Transactional(readOnly = true)
    public Prisoner findByUUID(UUID id) {
        return prisonerRepository.findById(id)
                .orElseThrow(() -> new PrisonerNotFoundException(id));
    }

    @Transactional
    public void deletePrisoner(UUID id)
    {
        prisonerRepository.deleteById(id);
    }

    @Transactional void savePrisoner(Prisoner prisoner){
        prisonerRepository.save(prisoner);
    }

    @Transactional
    public Prisoner addPrisoner(PrisonerDto prisonerDto) {
        final Prisoner prisoner = new Prisoner(prisonerDto);
        return prisonerRepository.save(prisoner);
    }

    @Transactional
    public Prisoner updatePrisoner(UUID id, PrisonerDto prisonerDto) {
        final Prisoner prisoner = findByUUID(id);
        prisoner.setName(prisonerDto.getName());
        prisoner.setArticle(prisonerDto.getArticle());
        prisoner.setBirthday(prisonerDto.getBirthday());
        prisoner.setTerm(prisonerDto.getTerm());
        prisoner.setReceiptDate(prisonerDto.getReceiptDate());
        return prisonerRepository.save(prisoner);
    }
}
