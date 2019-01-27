package com.acme.avaliacaoservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

	Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	public CommandLineRunner initDatabase(AvaliacaoRepository repository) {
		return args -> {
			logger.info("Preloading " + repository.save(new Avaliacao(1L, 2)));
			logger.info("Preloading " + repository.save(new Avaliacao(1L, 3)));
			logger.info("Preloading " + repository.save(new Avaliacao(3L, 2)));
			logger.info("Preloading " + repository.save(new Avaliacao(3L, 1)));
		};
	}
}
