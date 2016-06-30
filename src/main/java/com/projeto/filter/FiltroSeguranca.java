package com.projeto.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.projeto.model.User;

@WebFilter(urlPatterns = "/pages/*")
public class FiltroSeguranca implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession sessao = httpRequest.getSession();
		String contextPath = httpRequest.getContextPath();

		User usuario = (User) sessao.getAttribute("usuarioLogado");

		if (usuario == null || usuario.getEmail() == null) {
			httpResponse.sendRedirect(contextPath + "/");
		} else {
			chain.doFilter(request,response);
		} 

	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
