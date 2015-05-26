package com.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.projeto.util.Transactional;

public class GenericDAO<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	public EntityManager manager;

	@Inject
	public GenericDAO(EntityManager manager) {
		this.manager = manager;
	}

	@Transactional
	public T incluir(T obj) {
		manager.persist(obj);
		return obj;
	}

	@Transactional
	public T alterar(T obj) {
		manager.merge(obj);
		return obj;
	}

	@Transactional
	public T excluir(T obj) {
		obj = manager.merge(obj);
		manager.remove(obj);
		return obj;
	}

	@Transactional
	public List<T> listar(Class<T> classe) {
		StringBuilder sql = new StringBuilder();
		sql.append("select o from ");
		sql.append(classe.getSimpleName());
		sql.append(" o");

		@SuppressWarnings("unchecked")
		List<T> result = manager.createQuery(sql.toString()).getResultList();
		return result;
	}

	@Transactional
	public T buscar(Class<T> classe, Object obj) {
		T objRes = manager.find(classe, obj);
		return objRes;
	}

}
