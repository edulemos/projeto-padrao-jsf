package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Usuario;
import com.projeto.service.NegocioException;
import com.projeto.service.UsuarioService;
import com.projeto.util.JsfUtil;

@Named
@ViewScoped
public class UsuarioController extends JsfUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean isError;
	private List<Usuario> listaUsuarios;

	@Inject
	UsuarioService service;

	@Inject
	Usuario usuario;

	@PostConstruct
	public void init() {
		listaUsuarios = service.listar();
	}

	public void salvar() {
		try {
			service.salvar(usuario);
			listaUsuarios = service.listar();
			addInfoMessage("Usuário salvo com sucesso.");
			isError = false;
		} catch (NegocioException e) {
			isError = true;
			addNegocioException(e);
		} catch (Exception e) {
			addErrorMessage(e.getMessage());
		}
	}

	public void pesquisar() {
		listaUsuarios = service.pesquisar(usuario);
	}

	public void excluir(Usuario usuario) {
		service.excluir(usuario);
		listaUsuarios = service.listar();
		addInfoMessage("Usuário excluído com sucesso.");
	}

	public void selecionaUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

}
