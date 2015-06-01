package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Usuario;
import com.projeto.service.UsuarioService;
import com.projeto.util.JsfUtil;

@Named
@ViewScoped
public class UsuarioController extends JsfUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Usuario> listaUsuarios;

	@Inject
	UsuarioService service;

	@Inject
	Usuario usuario;

	@PostConstruct
	public void init() {
		listaUsuarios = service.listar();
	}
	
	public void salvar(){
		
	}
	
	public void selecionaUsuario(Usuario usuario){
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

}
