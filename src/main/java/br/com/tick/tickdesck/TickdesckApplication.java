package br.com.tick.tickdesck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class   TickdesckApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(TickdesckApplication.class, args);
	}

}
