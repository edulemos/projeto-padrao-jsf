package com.projeto.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.projeto.model.Role;

public class RoleDAO extends GenericDAO<Role> {
	private static final long serialVersionUID = 1L;

	@Inject
	public RoleDAO(EntityManager manager) {
		super(manager);
	}

}
