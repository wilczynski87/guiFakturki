package com.fakturki.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GuiFakturkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuiFakturkiApplication.class, args);
		log.info("\n\nThe GUI is on\n");
	}

}
