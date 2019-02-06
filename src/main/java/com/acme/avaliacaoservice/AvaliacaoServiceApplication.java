package com.acme.avaliacaoservice;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AvaliacaoServiceApplication {
	
	static final String MATRICULA = "NNNNNNNN";
	
	static final String EXCLUIR_AVALIACOES_POR_LIVROID_QUEUE_NAME = "excluir_avaliacoes_por_livroid_queue_" + MATRICULA;

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
			MessageConverter producerJackson2MessageConverter) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter);
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AvaliacaoServiceApplication.class, args);
	}
}
