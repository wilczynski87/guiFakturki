package com.fakturki.gui.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Product {
    private ProductEnum productEnum;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private int vatRate;
    private BigDecimal vatAmout;
    private BigDecimal price;
    private BigDecimal priceWithVat;
}