package com.projeto.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.projeto.dao.UsuarioDAO;
import com.projeto.model.Usuario;

public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioDAO dao;

	public boolean auntenticar(Usuario usuario) {
		Usuario u = dao.getUsuarioLogin(usuario);
		return null != u;
	}

	public void salvarUsuarioteste() {
		dao.salvarUsuarioteste();

	}

	public List<Usuario> listar() {
		return dao.listar(Usuario.class);
	}

}
