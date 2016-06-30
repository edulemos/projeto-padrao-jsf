package com.projeto.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Role;

@FacesConverter("RoleConverter")
public class RoleConverter implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null || value.equals("")) {
			return null;
		}

		Long id = new Long(value);

		Role role = new Role();
		role.setId(id);

		return role;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null) {
			return null;
		}

		Role role = (Role) value;

		return role.getName();
	}
}