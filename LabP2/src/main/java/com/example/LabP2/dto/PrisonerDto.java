package com.example.LabP2.dto;

import com.example.LabP2.models.Prisoner;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PrisonerDto {
    private UUID id;

    @NotBlank(message = "Ожидалось имя")
    @Size(min = 8, max = 64)
    private String name;

    @NotNull(message = "Ожидалась дата")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "Ожидалась дата")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;

    private Integer term;

    @NotBlank(message = "Ожидалась статья")
    @Size(min = 8, max = 64)
    private String article;

    public PrisonerDto(Prisoner prisoner) {
        this.id = prisoner.getId();
        this.name = prisoner.getName();
        this.article = prisoner.getArticle();;
        this.term = prisoner.getTerm();
        this.birthday =prisoner.getBirthday();
        this.receiptDate = prisoner.getReceiptDate();
    }
}
