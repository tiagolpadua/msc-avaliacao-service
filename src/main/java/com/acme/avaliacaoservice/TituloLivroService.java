package com.acme.avaliacaoservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class TituloLivroService {

	Logger logger = LoggerFactory.getLogger(TituloLivroService.class);

	@HystrixCommand(fallbackMethod = "getTituloDefault")
	public String getTitulo(Long livroId) {
		RestTemplate restTemplate = new RestTemplate();
		String livroResourceUrl = "http://localhost:8080/livros/";
		try {
			ResponseEntity<Livro> responseLivro = restTemplate.getForEntity(livroResourceUrl + livroId, Livro.class);
			return responseLivro.getBody().getTitulo();
		} catch (HttpClientErrorException ex) {
			if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
				return null;
			} else {
				logger.error("Ocorreu um erro na comunicação com o serviço de livros", ex);
				throw ex;
			}
		}
	}

	@SuppressWarnings("unused")
	private String getTituloDefault(Long livroId) {
		return "Erro ao consultar o título: " + livroId;
	}
}
