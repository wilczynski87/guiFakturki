package com.fakturki.gui.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClientTable {

    private String name;
    private String nip;
    private String invoice;
    private BigDecimal inDebt;

    public ClientTable(String name, String nip, String invoice, BigDecimal inDebt) {
        this.name = name;
        this.nip = nip;
        this.invoice = invoice;
        this.inDebt = inDebt;
    }
    public ClientTable() {
        this.name = "no name";
        this.nip = "no nip";
        this.invoice = null;
        this.inDebt = null;
    }
}