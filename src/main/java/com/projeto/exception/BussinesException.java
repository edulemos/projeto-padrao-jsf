package com.projeto.exception;

import java.util.List;

public class BussinesException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<String> erros;

	public BussinesException(List<String> erros) {
		this.erros = erros;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

}