package com.acme.avaliacaoservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name = "livros", url = "http://localhost:8080", fallback = LivroClient.LivroClientFallback.class)
@FeignClient(name = "livros", url = "http://localhost:8080", decode404 = true, fallback = LivroClient.LivroClientFallback.class)
public interface LivroClient {
	@RequestMapping(method = RequestMethod.GET, value = "/livros")
	List<Livro> getLivros();

	@RequestMapping(method = RequestMethod.GET, value = "/livros/{livroId}")
	Livro getLivroPorId(@PathVariable("livroId") Long livroId);

	@Component
	public static class LivroClientFallback implements LivroClient {

		@Override
		public List<Livro> getLivros() {
			return new ArrayList<Livro>();
		}

		@Override
		public Livro getLivroPorId(Long livroId) {
			return new Livro("Desconhecido", "Desconhecido", 0d);
		}
	}
}
