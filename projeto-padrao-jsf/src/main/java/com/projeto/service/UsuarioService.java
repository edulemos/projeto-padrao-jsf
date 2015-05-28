package com.projeto.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;

import com.projeto.dao.UsuarioDAO;
import com.projeto.model.Usuario;

public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioDAO dao;

	public Usuario usuarioLogin(Usuario usuario) {
		usuario.setSenha(encrypt(usuario.getSenha()));
		return dao.getUsuarioLogin(usuario);
	}

	public List<Usuario> listar() {
		return dao.listar(Usuario.class);
	}

	public String encrypt(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

	public void createBase() {
		dao.listar(Usuario.class);
	}

}
