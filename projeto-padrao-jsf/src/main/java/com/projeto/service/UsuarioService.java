package com.projeto.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.projeto.dao.UsuarioDAO;
import com.projeto.model.Usuario;
import com.projeto.util.ValidaDadosUtil;

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

	public void excluir(Usuario usuario) {
		dao.excluir(usuario);
	}

	public void salvar(Usuario usuario) throws NegocioException {

		validar(usuario);

		if (null != usuario.getId()) {
			dao.alterar(usuario);
		} else {
			usuario.setSenha(encrypt(usuario.getSenha()));
			dao.incluir(usuario);
		}

	}

	public List<Usuario> pesquisar(Usuario usuario) {
		return dao.pesquisar(usuario);
	}

	private void validar(Usuario usuario) throws NegocioException {
		List<String> erros = new ArrayList<String>();

		boolean editar = null != usuario.getId() && usuario.getId() > 0;

		if (ValidaDadosUtil.isNull(usuario.getNome())) {
			erros.add("O campo Nome precisa estar preenchido.");
		}

		if (ValidaDadosUtil.isNull(usuario.getEmail())) {
			erros.add("O campo Email precisa estar preenchido.");
		} else if (!ValidaDadosUtil.isEmailValid(usuario.getEmail())) {
			erros.add("O campo Email esta com valor invalido.");
		}

		if (ValidaDadosUtil.isNull(usuario.getPerfil())) {
			erros.add("O campo Perfil precisa ser selecionado.");
		}

		if (ValidaDadosUtil.isNull(usuario.getFlAtivo())) {
			erros.add("O campo Status precisa ser selecionado.");
		}

		if (!editar && ValidaDadosUtil.isNull(usuario.getSenha())) {
			erros.add("O campo Senha precisa ser selecionado.");
		} else if (!editar && usuario.getSenha().length() < 6) {
			erros.add("O campo ? deve ter no minimo 6 caracteres.");
		}

		if (!erros.isEmpty()) {
			throw new NegocioException(erros);
		}

	}

}
