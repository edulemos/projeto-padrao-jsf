package com.projeto.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Usuario;
import com.projeto.service.UsuarioService;

@Named
@ViewScoped
public class IndexController extends UtilController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioService service;

	@Inject
	Usuario usuario;

	@PostConstruct
	public void init(){
		service.salvarUsuarioteste();
	}	

	public String login() {
		
		if(service.auntenticar(usuario)){
            return "pages/home?faces-redirect=true";
		}else{
			addWarnMessage("Usuario e/ou senha invalido(s).");
			return "";
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
