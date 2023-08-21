package com.fakturki.gui.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Invoice {
    private Long id;
    private String number;
    private LocalDate date;
    private BigDecimal tax;
    private BigDecimal price;
    private BigDecimal priceWithTax;
    private List<Product> product;

    public static int getStandartTaxRate(ProductEnum product) {
        return switch (product) {
            case WODA -> 8;
            default -> 23;
        };
}
}
