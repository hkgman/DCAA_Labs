package com.example.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FilterOrderType {
    PRE(-1),
    POST(0),
    ROUTE(1);
    private final int order;
}
