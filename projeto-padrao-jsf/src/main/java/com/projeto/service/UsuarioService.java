package com.projeto.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.projeto.dao.UsuarioDAO;
import com.projeto.model.Usuario;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO dao;

	@Transactional
	public void salvar(Usuario usuario) {
		dao.salvar(usuario);
	}
	
	public void salvarUsuarioteste() {
		dao.insereUsuarioTeste();
	}
	
	public boolean auntenticar(Usuario usuario) {
		Usuario u = dao.login(usuario); 
		return null != u;
	}
}
