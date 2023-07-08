package com.fakturki.gui.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Client {

    private String name;
	private String address;
	private String nip;
	private String email;
	private boolean active;
    private List<Product> products;

}