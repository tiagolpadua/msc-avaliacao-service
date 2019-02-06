package com.acme.avaliacaoservice;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	Logger logger = LoggerFactory.getLogger(Receiver.class);

	private final AvaliacaoRepository repository;

	Receiver(AvaliacaoRepository repository) {
		this.repository = repository;
	}
	
	@RabbitListener(queues = AvaliacaoServiceApplication.EXCLUIR_AVALIACOES_POR_LIVROID_QUEUE_NAME)
	public void receiveMessageExcluirAvaliacoesPorLivro(Long id) throws InterruptedException {
		logger.info("Recebeu para exclusão por livroId: <" + id + ">");
		TimeUnit.SECONDS.sleep(3);
		repository.deleteAvaliacaoPorLivroId(id);
		logger.info("Processou exclusão por livroId: <" + id + ">");
	}
}