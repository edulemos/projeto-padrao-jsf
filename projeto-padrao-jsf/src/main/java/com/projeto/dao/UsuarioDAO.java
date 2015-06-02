package com.projeto.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.projeto.model.Usuario;
import com.projeto.util.Transactional;

public class UsuarioDAO extends GenericDAO<Usuario>{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public UsuarioDAO(EntityManager manager) {
		super(manager);	
	}
	
	@Transactional
	public Usuario getUsuarioLogin(Usuario usuario){
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

	public List<Usuario> pesquisar(Usuario usuario) {
		try {
			String jpql = "from Usuario where nome = :prm1";
			TypedQuery<Usuario> query = manager.createQuery(jpql, Usuario.class)
					.setParameter("prm1", usuario.getNome());
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
