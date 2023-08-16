package com.fakturki.gui.data;

public enum ProductEnum {
    PLAC("m2", "Wynajem powierzchni"),
    MIEJSCE_ZESTAW("szt","Miejsce postojowe dla zestawu"),
    MIEJSCE_SOLOWKA("szt","Miejsce postojowe dla ciężarówki"),
    MIEJSCE_NACZEPA("szt","Miejsce postojowe dla naczepy"),
    MIEJSCE_OSOBOWKA("szt","Miejsce postojowe dla samochodu 3,5t"),
    MIEJSCE_KONTENER_6("szt","Miejsce postojowe dla konteneru 6m"),
    MIEJSCE_KONTENER_12("szt","Miejsce postojowe dla konteneru 12m");

    private final String jm;
    private final String description;

    ProductEnum(String jm, String description) {
        this.jm = jm;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}