package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.projeto.model.Role;
import com.projeto.model.User;
import com.projeto.service.UsuarioService;
import com.projeto.util.JsfUtil;

/**
 * @author eduardo.lemos Controller das funcionalidades de usuários do sistema
 */
@Named
@ViewScoped
public class UsuarioController extends JsfUtil implements Serializable {

	private static final String PESQUISAR_USUARIOS = "pesquisar";

	final static Logger log = Logger.getLogger(UsuarioController.class);

	private static final long serialVersionUID = 1L;
	private List<User> listaUsuarios;
	private List<Role> rolesList;
	private String perfilCadastro;
	private User usuario = new User();

	@Inject
	UsuarioService service;

	@Inject
	User filtroPesquisa;

	@PostConstruct
	public void iniciar() {
		super.setAcao(super.getRequestAcao());
		log.info("Iniciou UsuarioController, ação --> " + acao);
	}

	public void salvar() {
		try {
			service.salvar(usuario, perfilCadastro);
			listaUsuarios = service.listar();
			this.acao = PESQUISAR_USUARIOS;
			usuario = new User();
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void pesquisar() {
		try {
			listaUsuarios = service.pesquisar(filtroPesquisa);
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void excluir(User user) {
		try {
			service.excluir(user);
			listaUsuarios = service.listar();
			this.usuario = new User();
			addInfoMessage("Usuario excluído com sucesso.");
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public String formulario(User user) {
		try {
			if(null !=user){
				this.usuario = user;
				rolesList = service.getRolesDisponiveiss(user);	
			}else{
				user = new User();
			}
			
		} catch (Exception e) {
			tratarErro(e);
		}
		return null;
	}

	public void atualizarPerfis() {
		try {
			usuario = service.buscarUsuarioPeloId(usuario);
			rolesList = service.getRolesDisponiveiss(usuario);
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void incluirPerfil(Role role) {
		try {
			service.IncluirRole(role, usuario);
			addInfoMessage("Perfil " + role.getName() + " incluído com sucesso.");
			atualizarPerfis();
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void removerPerfil(Role role) {
		try {
			service.excluirRole(role);
			addInfoMessage("Perfil " + role.getName() + " removido com sucesso.");
			atualizarPerfis();
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public void limparUsuario() {
		try {
			usuario = new User();
			perfilCadastro = null;
		} catch (Exception e) {
			tratarErro(e);
		}
	}

	public List<User> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<User> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public User getFiltroPesquisa() {
		return filtroPesquisa;
	}

	public void setFiltroPesquisa(User filtroPesquisa) {
		this.filtroPesquisa = filtroPesquisa;
	}

	public String getPerfilCadastro() {
		return perfilCadastro;
	}

	public void setPerfilCadastro(String perfilCadastro) {
		this.perfilCadastro = perfilCadastro;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}

}
