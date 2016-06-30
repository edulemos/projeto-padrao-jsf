package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.projeto.model.Role;
import com.projeto.model.User;
import com.projeto.service.UsuarioService;
import com.projeto.util.JsfUtil;

/**
 * @author eduardo.lemos Controller dos recursos externos ao sistema
 */
@Named
@ViewScoped
public class IndexController extends JsfUtil implements Serializable {

	final static Logger log = Logger.getLogger(IndexController.class);

	private static final long serialVersionUID = 1L;

	private List<Role> rolesList;
	private String confirmaSenha;
	private String perfil;
	private String key;

	@Inject
	UsuarioService userService;

	@Inject
	User usuario;

	public String login() {
		try {
			userService.verificarLogin(usuario);
			return "pages/home?faces-redirect=true";
		} catch (Exception e) {
			tratarErro(e);
			return "";
		}
	}

	public String logout() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			User usuarioLogado = (User) getSessionAttribute("usuarioLogado");
			log.info("[Usuário saiu do sistema]-->["+usuarioLogado+"]");
		} catch (Exception e) {
			tratarErro(e);
		}
		return "/login?faces-redirect=true";
	}

	public String cadastrar() {
		try {
			userService.cadastrarUsuario(usuario, perfil);
			usuario = new User();
		} catch (Exception e) {
			tratarErro(e);
		}
		return "";
	}

	public void recuperarSenha() {
		try {
			userService.enviaEmailRecuperarSenha(usuario.getEmail());
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void alterarSenha() {
		try {
			userService.alterarSenha(usuario, confirmaSenha, key);
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void carregarRoles() {
		rolesList = userService.getRoles();
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

}
