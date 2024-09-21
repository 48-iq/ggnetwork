package ru.ivanov.ggnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GgnetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GgnetworkApplication.class, args);
	}

}
