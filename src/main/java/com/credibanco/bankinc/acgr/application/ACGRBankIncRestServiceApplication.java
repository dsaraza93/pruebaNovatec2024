package com.credibanco.bankinc.acgr.application;

import com.credibanco.bankinc.acgr.controller.BankIncACGRController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;




/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */

@SpringBootApplication(scanBasePackages={"com.credibanco.bankinc.acgr"})
public class ACGRBankIncRestServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ACGRBankIncRestServiceApplication.class, args);
	}
}

