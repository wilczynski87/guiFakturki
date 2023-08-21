package com.fakturki.gui.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UtilityReading {
    
    private Long id;
    private ProductEnum utility;
    private LocalDate readingDate;
    private BigDecimal reading;
    private BigDecimal price;
    private String clientNip;
}