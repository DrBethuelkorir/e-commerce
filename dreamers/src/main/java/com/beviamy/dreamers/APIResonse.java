package com.beviamy.dreamers;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class APIResonse {
    private String message;
    private Object data;
}
