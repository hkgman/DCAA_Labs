package com.example.gateway.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SortRequest {
    private int[] array;
    private int count;
}
