package com.example.LabP2.models;

import com.example.LabP2.dto.PrisonerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Prisoner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 64)
    @Size(min = 4, max = 64)
    private String name;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @Column(nullable = false)
    @Size(min=1)
    private Integer term;

    @Column(nullable = false, length = 64)
    @Size(min = 4, max = 64)
    private String article;

    public Prisoner(PrisonerDto prisonerDto){
        this.id=prisonerDto.getId();
        this.name = prisonerDto.getName();
        this.article = prisonerDto.getArticle();
        this.birthday = prisonerDto.getBirthday();
        this.receiptDate = prisonerDto.getReceiptDate();
        this.term = prisonerDto.getTerm();
    }
}
