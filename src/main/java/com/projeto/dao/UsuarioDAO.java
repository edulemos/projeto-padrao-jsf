package com.projeto.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.projeto.model.User;
import com.projeto.util.Transactional;

public class UsuarioDAO extends GenericDAO<User> {
	private static final long serialVersionUID = 1L;

	@Inject
	public UsuarioDAO(EntityManager manager) {
		super(manager);
	}

	@Transactional
	public User getUsuarioLogin(User user) {
		try {
			String jpql = "from User where email = :prm1 and password = :prm2";
			TypedQuery<User> query = manager.createQuery(jpql, User.class)
					.setParameter("prm1", user.getEmail())
					.setParameter("prm2", user.getPassword());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public User getUsuarioByEmail(String email) {
		try {
			String jpql = "from User where email = :prm1";
			TypedQuery<User> query = manager.createQuery(jpql, User.class).setParameter("prm1", email);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public List<User> pesquisar(User filtroPesquisa) throws Exception {

		try {

			boolean porEmail = filtroPesquisa.getEmail() != null && !filtroPesquisa.getEmail().isEmpty();
			boolean porNome = filtroPesquisa.getName() != null && !filtroPesquisa.getName().isEmpty();

			StringBuilder jpql = new StringBuilder();
			jpql.append("from User where 1=1");

			if (porEmail) {
				jpql.append(" and upper(email) like :email");
			}

			if (porNome) {
				jpql.append(" and upper(name) like :nome");
			}

			TypedQuery<User> query = manager.createQuery(jpql.toString(), User.class);

			if (porEmail) {
				query.setParameter("email", "%" + filtroPesquisa.getEmail().toUpperCase() + "%");
			}

			if (porNome) {
				query.setParameter("nome", "%" + filtroPesquisa.getName().toUpperCase() + "%");
			}

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

}
