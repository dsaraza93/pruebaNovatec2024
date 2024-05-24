package com.credibanco.bankinc.dfsv.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */

@SpringBootApplication(scanBasePackages={"com.credibanco.bankinc.dfsv"})
public class BankIncRestServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankIncRestServiceApplication.class, args);
	}
}

