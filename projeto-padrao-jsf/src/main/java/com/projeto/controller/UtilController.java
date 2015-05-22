package com.projeto.controller;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public abstract class UtilController {

    public void addErrorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
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

}
