package com.acme.avaliacaoservice;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
	public float getAvaliacaoPorLivroId(@PathVariable Long id) {
		logger.info("getAvaliacaoPorLivroId: " + id);
		throw new NotImplementedException();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao adicionarAvaliacao(@RequestBody Avaliacao avalicacao) {
		logger.info("adicionarAvaliacao: " + avalicacao);
		return repository.save(avalicacao);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirAvaliacao(@PathVariable Long id) {
		logger.info("excluirAvaliacao: " + id);
		repository.deleteById(id);
	}
}
