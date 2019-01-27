package com.acme.avaliacaoservice;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Avaliacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Id @GeneratedValue Long id;
	private Long livroId;
	private int nota;

	public Avaliacao() {
		super();
	}
	
	public Avaliacao(Long livroId, int nota) {
		super();
		this.livroId = livroId;
		this.nota = nota;
	}

	public Avaliacao(Long id, Long livroId, int nota) {
		super();
		this.id = id;
		this.livroId = livroId;
		this.nota = nota;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLivroId() {
		return livroId;
	}

	public void setLivroId(Long livroId) {
		this.livroId = livroId;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	@Override
	public String toString() {
		return "Avaliacao [id=" + id + ", livroId=" + livroId + ", nota=" + nota + "]";
	}

}
