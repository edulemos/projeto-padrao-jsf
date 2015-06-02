package com.projeto.service;

import java.util.List;

public class NegocioException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<String> erros;

	public NegocioException(List<String> erros) {
		this.erros = erros;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

}