package com.projeto.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.projeto.model.Usuario;

public class UsuarioDAO  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private EntityManager manager;

	@Inject
	public UsuarioDAO(EntityManager manager) {
		this.manager = manager;
	}
	
	public void salvar(Usuario usuario){
		manager.persist(usuario);
	}
	
	public void insereUsuarioTeste() {
		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		Usuario usuario = new Usuario();
		usuario.setNome("admin");
		usuario.setSenha("123456");
		manager.persist(usuario);
		trx.commit();		
	}
	
	public Usuario login(Usuario usuario){
		try {
			String jpql = "from Usuario where nome = :prm1 and senha = :prm2";
			TypedQuery<Usuario> query = manager.createQuery(jpql, Usuario.class)
					.setParameter("prm1", usuario.getNome())
					.setParameter("prm2", usuario.getSenha());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
