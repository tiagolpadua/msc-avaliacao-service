package com.acme.avaliacaoservice;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "livros", url = "http://localhost:8080")
public interface LivroClient {
	@RequestMapping(method = RequestMethod.GET, value = "/livros")
	List<Livro> getLivros();

	@RequestMapping(method = RequestMethod.GET, value = "/livros/{livroId}")
	Livro getLivroPorId(@PathVariable("livroId") Long livroId);
}
