package com.product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String messages;

    private List<String> details;
}
