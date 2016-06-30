package com.projeto.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailUtil {

	private String conteudoHtml;
	private String caminhoTemplate;
	private String destinatario;
	private String assunto;
	private HashMap<String, String> parametros;
	Properties propertie = new Properties();

	public void enviarEmailHtml() throws IOException {

		propertie.load(Thread.currentThread().getContextClassLoader().getResource("email.properties").openStream());

		Thread thead = new Thread(new ThreadMail());
		thead.start();
	}

	public String getTemplateHtml() {

		StringBuffer sbf = new StringBuffer();
		String templateStr = "";

		try {
			// carrega o template
			BufferedReader br = new BufferedReader(new FileReader(caminhoTemplate));
			while (br.ready()) {
				sbf.append(br.readLine());
			}

			templateStr = sbf.toString();

			// carrega os parametros do template
			for (Entry<String, String> entry : parametros.entrySet()) {
				templateStr = templateStr.replace(entry.getKey(), entry.getValue());
			}

			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// retorna o template sem espacos
		return templateStr.replaceAll("\\s+", " ");
	}

	public class ThreadMail extends Thread {

		String hostName;
		String fromEmail;
		String usuario;
		String senha;
		String conteudoHtml;
		String destinatario;
		String assunto;
		boolean authenticator;
		boolean tls;
		boolean ssl;
		int smtpPort;

		public ThreadMail() {

			hostName = propertie.getProperty("email.hostName");
			fromEmail = propertie.getProperty("email.fromEmail");
			usuario = propertie.getProperty("email.usuario");
			senha = propertie.getProperty("email.senha");
			smtpPort = null != propertie.getProperty("email.smtpPort") ? new Integer(propertie.getProperty("email.smtpPort")) : 25;
			authenticator = "true".equals(propertie.getProperty("email.authenticator")) ? true : false;
			tls = "true".equals(propertie.getProperty("email.tls")) ? true : false;
			ssl = "true".equals(propertie.getProperty("email.ssl")) ? true : false;

		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {

			try {

				HtmlEmail htmlEmail = new HtmlEmail();
				String templateHtml = getTemplateHtml();
				System.out.println(templateHtml);
				htmlEmail.setHtmlMsg(templateHtml);
				htmlEmail.addTo(getDestinatario());
				htmlEmail.setSubject(getAssunto());

				htmlEmail.setFrom(fromEmail);
				htmlEmail.setHostName(hostName);
				htmlEmail.setSmtpPort(smtpPort);
				htmlEmail.setTLS(tls);
				htmlEmail.setSSL(ssl);

				if (authenticator) {
					htmlEmail.setAuthenticator(new DefaultAuthenticator(usuario, senha));
				}

				htmlEmail.send();

			} catch (EmailException e) {
				e.printStackTrace();
			}

		}

	}

	public String getConteudoHtml() {
		return conteudoHtml;
	}

	public void setConteudoHtml(String conteudoHtml) {
		this.conteudoHtml = conteudoHtml;
	}

	public String getCaminhoTemplate() {
		return caminhoTemplate;
	}

	public void setCaminhoTemplate(String caminhoTemplate) {
		this.caminhoTemplate = caminhoTemplate;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public HashMap<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(HashMap<String, String> parametros) {
		this.parametros = parametros;
	}

}
