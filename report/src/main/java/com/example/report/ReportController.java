package com.example.report;

import com.example.report.Dto.PrisonerReportDto;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@Slf4j
public class ReportController {
    private final ReportService reportService;
    private final KafkaProducerService kafkaProducerService;
    private final RestTemplate restTemplate;
    private final static String CORID = "correlation-id";
    private HttpHeaders getHeaders(String corId, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CORID, corId);
        log.info(String.format("CORID: %s /report %s ", corId, name));
        return headers;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrisonerReportDto>> getPosts(@RequestHeader("correlation-id") String corId) {
        HttpHeaders headers = getHeaders(corId,"getPosts");
        log.info(corId);
        String csvFileName = corId+ ".csv";
        List<PrisonerReportDto> prisoners = reportService.getPrisoners();
        File csvFile = generateCsv(prisoners, csvFileName);

        uploadFileToMinio(corId,csvFile, headers);

        if (csvFile.exists()) {
            csvFile.delete();
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok().headers(headers).body(prisoners);
    }
    private File generateCsv(List<PrisonerReportDto> prisoners, String fileName) {
        File csvFile = new File(fileName);
        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(new FileOutputStream(csvFile), "Windows-1251"),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            String[] header = {"name", "receiptDate", "endDate", "status"};
            writer.writeNext(header);

            for (PrisonerReportDto prisoner : prisoners) {
                String[] data = {
                        prisoner.getName(),
                        prisoner.getReceiptDate().toString(),
                        prisoner.getEndDate().toString(),
                        prisoner.getStatus()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            log.error("Error generating CSV", e);
        }
        return csvFile;
    }

    private void uploadFileToMinio(String key,File file,HttpHeaders curHeaders) {
        try{
            kafkaProducerService.sendMessage(key,Files.readAllBytes(file.toPath()));
        }catch (Exception e)
        {
            log.error("Error send to kafka",e);
        }

    }
}