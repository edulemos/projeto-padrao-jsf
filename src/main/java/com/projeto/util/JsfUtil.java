package com.projeto.util;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.projeto.exception.BussinesException;
import com.projeto.model.User;

public abstract class JsfUtil {
	final static Logger log = Logger.getLogger(JsfUtil.class);

	protected String acao;

	public User getUsuarioLogado(){
		return (User) getSessionAttribute("usuarioLogado");
	}

	public String getRequestAcao() {
		String acaoPrm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("acao");
		return null != acaoPrm ? acaoPrm : "";
	}

	public void tratarErro(Exception exception) {

		if (exception instanceof BussinesException) {
			BussinesException bussinesException = (BussinesException) exception;
			for (String erro : bussinesException.getErros()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, erro, null));
				log.error("[Ocorreu um erro de negócio]-->["+ erro+"]");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
			log.error("[Ocorreu um erro innesperado]-->["+exception.getMessage() + " - "+ exception.getCause() +"]", exception);
		}

	}

	public void addInfoMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}

	public void addWarnMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
	}

	@SuppressWarnings("rawtypes")
	public static Object getSessionAttribute(String attributeName) {
		try {
			ExternalContext ec = getExternalContext();
			if (ec != null) {
				Map attrMap = ec.getSessionMap();
				if (attrMap != null) {
					return attrMap.get(attributeName);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setSessionAttribute(String attributeName, Object attributeValue) {
		try {
			ExternalContext ec = getExternalContext();
			if (ec != null) {
				Map attrMap = ec.getSessionMap();
				if (attrMap != null) {
					attrMap.put(attributeName, attributeValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ExternalContext getExternalContext() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (facesContext == null) {
				return null;
			} else {
				return facesContext.getExternalContext();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

}
