package com.example.gateway.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SortResponse {
    private int[] array;
    private Long time;
}
