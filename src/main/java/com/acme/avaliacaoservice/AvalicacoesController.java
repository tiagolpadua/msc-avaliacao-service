package com.acme.avaliacaoservice;

import java.io.IOException;
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

import feign.FeignException;

@RestController
@RequestMapping("/avaliacoes")
public class AvalicacoesController {

	Logger logger = LoggerFactory.getLogger(AvalicacoesController.class);

	private final AvaliacaoRepository repository;

	private final LivroClient livroClient;

	AvalicacoesController(AvaliacaoRepository repository, LivroClient livroClient) {
		this.repository = repository;
		this.livroClient = livroClient;
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

	@DeleteMapping("/livro/{livroId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAvaliacaoPorLivroId(@PathVariable Long livroId) {
		logger.info("deleteAvaliacaoPorLivroId: " + livroId);
		repository.deleteAvaliacaoPorLivroId(livroId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao adicionarAvaliacao(@RequestBody Avaliacao avaliacao) throws IOException {
		logger.info("adicionarAvaliacao: " + avaliacao);
		String tituloLivro = livroClient.getLivroPorId(avaliacao.getLivroId()).getTitulo();
		if (tituloLivro == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não existe: " + avaliacao.getLivroId());
		}
		logger.info("Título do livro avaliado: " + tituloLivro);
		return repository.save(avaliacao);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirAvaliacao(@PathVariable Long id) {
		logger.info("excluirAvaliacao: " + id);
		repository.deleteById(id);
	}
}
