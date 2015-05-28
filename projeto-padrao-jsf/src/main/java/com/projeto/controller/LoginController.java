package com.projeto.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Usuario;
import com.projeto.service.UsuarioService;
import com.projeto.util.JsfUtil;

@Named
@ViewScoped
public class LoginController extends JsfUtil implements Serializable {

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
		
		Usuario usuarioLogin = service.usuarioLogin(usuario);
		
		if(null != usuarioLogin){
			setSessionAttribute("usuario", usuario);
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
