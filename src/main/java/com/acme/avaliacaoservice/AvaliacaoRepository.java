package com.acme.avaliacaoservice;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>, JpaSpecificationExecutor<Avaliacao> {
	@Transactional
	@Modifying
	@Query("delete from Avaliacao a where livroId = ?1")
    void deleteAvaliacaoPorLivroId(Long livroId);
}
