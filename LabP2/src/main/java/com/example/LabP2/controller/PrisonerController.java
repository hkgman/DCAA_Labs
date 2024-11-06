package com.example.LabP2.controller;

import com.example.LabP2.dto.PrisonerDto;
import com.example.LabP2.service.PrisonerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prisoners")
@Slf4j
public class PrisonerController {
    private final RestTemplate restTemplate;
    private final static String CORID = "correlation-id";
    private final PrisonerService prisonerService;

    private HttpHeaders getHeaders(String corId, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CORID, corId);
        log.info(String.format("CORID: %s /prisoners %s ", corId, name));
        return headers;
    }

    @PostMapping
    public ResponseEntity<PrisonerDto> createPrisoner(@RequestBody @Valid PrisonerDto prisonerDto, @RequestHeader(value = CORID) String corId) {

        return new ResponseEntity<>(new PrisonerDto(prisonerService.addPrisoner(prisonerDto)), getHeaders(corId, "createPrisoner"), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrisonerDto> updatePrisoner(@PathVariable UUID id,
                                                      @RequestBody @Valid PrisonerDto prisonerDto,
                                                      @RequestHeader(value = CORID) String corId) {
        return new ResponseEntity<>(new PrisonerDto(prisonerService.updatePrisoner(id, prisonerDto)), getHeaders(corId, "updatePrisoner"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrisonerDto> getPrisoner(@PathVariable UUID id, @RequestHeader(value = CORID) String corId) {
        return new ResponseEntity<>(new PrisonerDto(prisonerService.findByUUID(id)), getHeaders(corId, "getPrisoner"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrisoner(@PathVariable UUID id, @RequestHeader(value = CORID) String corId) {
        prisonerService.deletePrisoner(id);
        return new ResponseEntity<>(getHeaders(corId, "deletePrisoner"), HttpStatus.OK);
    }


    @GetMapping("/report")
    public ResponseEntity<Object> externalReport(@RequestHeader(value = CORID) String corId) {
        String externalServiceUrl = "http://report:8082/report";
        HttpHeaders headers = getHeaders(corId, "externalReport");
        ResponseEntity<Object> response = restTemplate.exchange(
                externalServiceUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }

}
