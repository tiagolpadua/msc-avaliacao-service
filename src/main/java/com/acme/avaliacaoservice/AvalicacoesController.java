package com.acme.avaliacaoservice;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao adicionarAvaliacao(@RequestBody Avaliacao avalicacao) throws IOException {
		logger.info("adicionarAvaliacao: " + avalicacao);

		RestTemplate restTemplate = new RestTemplate();
		String livroResourceUrl = "http://localhost:8080/livros/";
		
		ResponseEntity<Livro> responseLivro = restTemplate.getForEntity(livroResourceUrl + avalicacao.getLivroId(), Livro.class); 
				
		logger.info("responseLivro.getBody(): " + responseLivro.getBody());
		
		return repository.save(avalicacao);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirAvaliacao(@PathVariable Long id) {
		logger.info("excluirAvaliacao: " + id);
		repository.deleteById(id);
	}
}
