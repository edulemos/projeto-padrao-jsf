package com.projeto.service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.projeto.dao.RoleDAO;
import com.projeto.dao.UsuarioDAO;
import com.projeto.enumeration.RolesEnum;
import com.projeto.exception.BussinesException;
import com.projeto.model.Role;
import com.projeto.model.User;
import com.projeto.util.EmailUtil;
import com.projeto.util.JsfUtil;

public class UsuarioService extends JsfUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger log = Logger.getLogger(UsuarioService.class);

	@Inject
	UsuarioDAO userDao;

	@Inject
	RoleDAO roleDao;

	/**
	 * Verifica os dados de login
	 * 
	 * @param usuario
	 * @return usuario
	 * @throws BussinesException
	 */
	public void verificarLogin(User usuario) throws BussinesException {
		usuario.setPassword(encrypt(usuario.getPassword()));
		User usuarioLogin = userDao.getUsuarioLogin(usuario);
		if (null != usuarioLogin) {
			setSessionAttribute("usuarioLogado", usuarioLogin);
			log.info("[Usuário entrou no sistema]-->["+usuarioLogin+"]");
		} else {
			log.info("[Dados de Login inválidos]-->["+ usuarioLogin+"]");
			throw new BussinesException(Arrays.asList("Usuário e/ou senha inválido(s)."));
		}
	}

	/**
	 * Lista todos usuarios
	 * 
	 * @return lista de usuários
	 */
	public List<User> listar() {
		return userDao.listar(User.class);
	}

	/**
	 * Ecrypta string em MD5
	 * 
	 * @param string
	 * @return string criptografada
	 */
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

	/**
	 * Exclui usuário do sistema
	 * 
	 * @param usuario
	 */
	public void excluir(User usuario) {
		userDao.excluir(usuario);
		log.info("[Usuário excluído]-->[" + usuario + "]");
	}

	/**
	 * Cadastra usuário no sistema
	 * 
	 * @param usuario
	 * @param perfil
	 * @throws BussinesException
	 */
	public void cadastrarUsuario(User usuario, String perfil) throws BussinesException {

		User userBYEmail = userDao.getUsuarioByEmail(usuario.getEmail());

		if (null != userBYEmail) {
			throw new BussinesException(Arrays.asList("Email já cadastrado"));
		}

		Role role = new Role(perfil);
		role.setUser(usuario);
		usuario.setAuthorities(Arrays.asList(role));

		usuario.setPassword(encrypt(usuario.getPassword()));
		userDao.incluir(usuario);
		
		addInfoMessage("Usuário cadastrado com sucesso.");
		log.info("Usuário cadastrado --> " + usuario);
		
	}

	/**
	 * Salva o usuário
	 * @param perfilCadastro 
	 * 
	 * @param usuario
	 * @throws BussinesException
	 */
	public void salvar(User user, String perfilCadastro) throws BussinesException {
		User userMail = userDao.getUsuarioByEmail(user.getEmail());

		boolean emailJaCadastrado = userMail != null;
		boolean novoUsuario = user.getId() == null;
		boolean mesmoUsuario = !novoUsuario && emailJaCadastrado && user.getId().equals(userMail.getId());

		if (emailJaCadastrado && novoUsuario) {
			throw new BussinesException(Arrays.asList("Email já cadastrado"));
		} else if (emailJaCadastrado && !novoUsuario && !mesmoUsuario) {
			throw new BussinesException(Arrays.asList("Email já cadastrado"));
		}

		if (novoUsuario) {
			user.setPassword(encrypt(user.getPassword()));			
			userDao.incluir(user);
			
			Role role = new Role(perfilCadastro);
			role.setUser(user);
			roleDao.incluir(role);
			
			addInfoMessage("Usuario Incluído com sucesso");
		} else {
			userDao.alterar(user);
			addInfoMessage("Usuario Alterado com sucesso");
		}
		
	}

	/**
	 * Monta as roles disponiveis no sistema
	 * 
	 * @return lista de roles
	 */
	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		Role role = null;
		for (RolesEnum r : RolesEnum.values()) {
			role = new Role();
			role.setName(r.getValue());
			role.setLabel(r.name());
			roles.add(role);
		}
		return roles;
	}

	/**
	 * Montas as roles dos sistema, removemdo as ja associadas
	 * 
	 * @param user
	 * @return
	 */
	public List<Role> getRolesDisponiveiss(User user) {
		List<Role> roles = new ArrayList<Role>();
		String userRoles = "";
		if (null != user && !user.getAuthorities().isEmpty()) {
			for (Role r : user.getAuthorities()) {
				userRoles += r.getName() + "#";
			}
		}
		Role role = null;
		for (RolesEnum r : RolesEnum.values()) {
			if (userRoles.contains(r.getValue())) {
				continue;
			}
			role = new Role();
			role.setName(r.getValue());
			role.setLabel(r.name());
			roles.add(role);
		}

		return roles;
	}

	/**
	 * Envia o email de recuperação de senha
	 * 
	 * @param email
	 * @throws BussinesException
	 * @throws IOException
	 */
	public void enviaEmailRecuperarSenha(String email) throws BussinesException, IOException {
		log.info("Enviando o email de reucuperação para:" + email);

		User userMail = userDao.getUsuarioByEmail(email);

		if (userMail == null) {
			throw new BussinesException(Arrays.asList("Email não encontrado"));
		}

		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String pathTemplate = ctx.getRealPath("/email/emailRecuperarSenha.html");

		HashMap<String, String> parametros = new HashMap<String, String>();
		parametros.put("$P{email}", email);
		parametros.put("$P{rooturl}", getRootURL());
		parametros.put("$P{key}", gerarChaveSeguranca(email));

		EmailUtil emailUtil = new EmailUtil();
		emailUtil.setAssunto("Recuperar Senha");
		emailUtil.setDestinatario(email);
		emailUtil.setCaminhoTemplate(pathTemplate);
		emailUtil.setParametros(parametros);
		emailUtil.enviarEmailHtml();
		
		addInfoMessage("Email enviado, verifique sua caixa de correio");
		log.info("Email de recuperação de senha enviado -->  " + email);

	}

	/**
	 * Gera a chave de segurança de recupera rsenha
	 * 
	 * @param email
	 * @return
	 */
	private String gerarChaveSeguranca(String email) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String dateKey = sdf.format(new Date());
		return encrypt(email + dateKey + dateKey.hashCode());
	}

	/**
	 * Retorna a Url raiz do sistema, para montar os links do email
	 * 
	 * @return
	 */
	public String getRootURL() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Object request = facesContext.getExternalContext().getRequest();
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest) request);
			String path = req.getRequestURL().toString().replace(req.getServletPath(), "");
			return path;
		}
		return null;
	}

	/**
	 * Altera a senha do usuário
	 * 
	 * @param usuario
	 * @param confirmaSenha
	 * @param key
	 * @throws BussinesException
	 */
	public void alterarSenha(User usuario, String confirmaSenha, String key) throws BussinesException {

		if (key.equals(gerarChaveSeguranca(usuario.getEmail()))) {
			throw new BussinesException(Arrays.asList("Chave de verificação vencida ou inválida."));
		}

		if (!usuario.getPassword().equals(confirmaSenha)) {
			throw new BussinesException(Arrays.asList("Senhas diferentes."));
		}

		User user = userDao.getUsuarioByEmail(usuario.getEmail());
		user.setPassword(encrypt(confirmaSenha));
		userDao.alterar(user);
		
		addInfoMessage("Senha alterada");
		log.info("Senha Alterada --> " + usuario.getEmail());

	}

	/**
	 * Pesquisa usuários
	 * 
	 * @param filtroPesquisa
	 * @return lista
	 * @throws Exception
	 */
	public List<User> pesquisar(User filtroPesquisa) throws Exception {
		log.info("Pesquisando usuários com os filtros --> " + filtroPesquisa);
		List<User> lista = userDao.pesquisar(filtroPesquisa);
		log.info("Retornados --> " + lista.size() + " registros");
		return lista;
	}

	/**
	 * Exclui uma role do usuário
	 * 
	 * @param role
	 */
	public void excluirRole(Role role) {
		roleDao.excluir(role);
	}

	/**
	 * Adiciona uma role ao usuário
	 * 
	 * @param role
	 * @param usuario
	 */
	public void IncluirRole(Role role, User usuario) {
		role.setUser(usuario);
		roleDao.incluir(role);
		log.info("Perfil --> " + role + " incluido ao usuario --> " + usuario);
	}

	/**
	 * Busca um usuário pelo id
	 * 
	 * @param user
	 * @return
	 */
	public User buscarUsuarioPeloId(User user) {
		return userDao.buscar(User.class, user.getId());
	}

}
