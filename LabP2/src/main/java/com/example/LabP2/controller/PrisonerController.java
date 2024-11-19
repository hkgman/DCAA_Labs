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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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


    @GetMapping("/fileFromMinio/{filename}")
    public ResponseEntity<byte[]> getFile(@RequestHeader(value = CORID) String corId, @PathVariable String filename) throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<byte[]>> future = CompletableFuture.supplyAsync(() -> getFileFromMinio(corId,filename))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[] {}));
        return future.get();
    }




    private ResponseEntity<byte[]> getFileFromMinio(String corId,String filename) {
        String externalServiceUrl = "http://myminio:8081/download/" + filename;
        HttpHeaders headers = getHeaders(corId, "getFileFromMinio");
        log.info("чвчв hihi");
        ResponseEntity<byte[]> response = restTemplate.exchange(
                externalServiceUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );
        log.info("hoho hihi");
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }
}
