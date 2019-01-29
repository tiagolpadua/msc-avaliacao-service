package com.acme.avaliacaoservice;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/avaliacoes")
public class AvalicacoesController {

	Logger logger = LoggerFactory.getLogger(AvalicacoesController.class);

	private final AvaliacaoRepository repository;

	AvalicacoesController(AvaliacaoRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Avaliacao> getAvaliacoes() {
		logger.info("getAvaliacoes");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Avaliacao getAvaliacaoPorId(@PathVariable Long id) {
		logger.info("getAvaliacaoPorId: " + id);
		return repository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avalicacao não encontrada: " + id));
	}

	@GetMapping("/livro/{id}")
	public Avaliacao getAvaliacaoPorLivroId(@PathVariable Long id) {
		logger.info("getAvaliacaoPorLivroId: " + id);
		throw new RuntimeException("Não implementado");
	}
	
	@DeleteMapping("/livro/{livroId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAvaliacaoPorLivroId(@PathVariable Long livroId) {
		logger.info("deleteAvaliacaoPorLivroId: " + livroId);
		repository.deleteAvaliacaoPorLivroId(livroId);
	}
	
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
	    int timeout = 10000;
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
	      = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(timeout);
	    clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
	    clientHttpRequestFactory.setReadTimeout(timeout);
	    return clientHttpRequestFactory;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao adicionarAvaliacao(@RequestBody Avaliacao avaliacao) throws IOException {
		logger.info("adicionarAvaliacao: " + avaliacao);

		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		String livroResourceUrl = "http://localhost:8080/livros/";

		try {
			restTemplate.getForEntity(livroResourceUrl + avaliacao.getLivroId(), Livro.class);
			logger.error("Livro " + avaliacao.getLivroId() + " localizado");
		} catch (HttpClientErrorException ex) {
			logger.error("Ocorreu um erro na comunicação com o serviço de livros", ex);
			if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Livro vinculado a avaliação não foi encontrado.");
			} else {
				throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
						"Ocorreu um erro não esperado na comunicação com o serviço de livros: " + ex.getMessage());
			}
		} catch (ResourceAccessException ex) {
			logger.error("Ocorreu um erro na comunicação com o serviço de livros", ex);
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
					"Ocorreu um erro não esperado na comunicação com o serviço de livros: " + ex.getMessage());
		}

		return repository.save(avaliacao);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirAvaliacao(@PathVariable Long id) {
		logger.info("excluirAvaliacao: " + id);
		repository.deleteById(id);
	}
}
