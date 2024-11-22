package com.example.gateway.controller;

import com.example.gateway.dto.SortRequest;
import com.example.gateway.dto.SortResponse;
import lombok.extern.slf4j.Slf4j;
import org.merge.sort.ParallelMergeSort;
import org.merge.sort.SortResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sort")
@Slf4j
public class SortController {
    @PostMapping
    public ResponseEntity<SortResponse> sort(@RequestBody SortRequest sq) {
        try {
            if (sq == null || sq.getArray() == null || sq.getCount() <= 0) {
                return ResponseEntity.badRequest().body(null);
            }
            log.info("privet");
            ParallelMergeSort mergeSort = new ParallelMergeSort();
            int[] array = sq.getArray();
            SortResult sortResult = mergeSort.sort(array, sq.getCount());
            SortResponse  sortResponse= new SortResponse();
            sortResponse.setArray(sortResult.result);
            sortResponse.setTime(sortResult.timeResult);
            return ResponseEntity.ok(sortResponse);

        } catch (Exception e) {
            log.error("Error occurred while sorting", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SortResponse());
        }
    }
}
