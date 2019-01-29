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

@RestController
@RequestMapping("/avaliacoes")
public class AvalicacoesController {

	Logger logger = LoggerFactory.getLogger(AvalicacoesController.class);

	private final AvaliacaoRepository repository;

	private final TituloLivroService tituloLivroService;

	AvalicacoesController(AvaliacaoRepository repository, TituloLivroService tituloLivroService) {
		this.repository = repository;
		this.tituloLivroService = tituloLivroService;
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

//	private ClientHttpRequestFactory getClientHttpRequestFactory() {
//		int timeout = 10000;
//		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectTimeout(timeout);
//		clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
//		clientHttpRequestFactory.setReadTimeout(timeout);
//		return clientHttpRequestFactory;
//	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Avaliacao adicionarAvaliacao(@RequestBody Avaliacao avaliacao) throws IOException {
		logger.info("adicionarAvaliacao: " + avaliacao);

		String tituloLivro = tituloLivroService.getTitulo(avaliacao.getLivroId());
		if (tituloLivro != null) {
			logger.info("Título do livro avaliado: " + tituloLivro);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não existe: " + avaliacao.getLivroId());
		}

//		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
//		String livroResourceUrl = "http://localhost:8080/livros/";

//		try {
//			ResponseEntity<Livro> responseLivro = restTemplate.getForEntity(livroResourceUrl + avaliacao.getLivroId(),
//					Livro.class);
//			logger.info("Livro " + responseLivro.getBody().getTitulo() + " localizado");
//		} catch (HttpClientErrorException ex) {
//			logger.error("Ocorreu um erro na comunicação com o serviço de livros", ex);
//			if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//						"Livro vinculado a avaliação não foi encontrado.");
//			} else {
//				throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
//						"Ocorreu um erro não esperado na comunicação com o serviço de livros: " + ex.getMessage());
//			}
//		} catch (ResourceAccessException ex) {
//			logger.error("Ocorreu um erro na comunicação com o serviço de livros", ex);
//			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
//					"Ocorreu um erro não esperado na comunicação com o serviço de livros: " + ex.getMessage());
//		}

		return repository.save(avaliacao);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirAvaliacao(@PathVariable Long id) {
		logger.info("excluirAvaliacao: " + id);
		repository.deleteById(id);
	}
}
