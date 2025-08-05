package org.ace.java.web.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ace.accounting.role.Role;
import org.ace.accounting.system.webPage.WebPage;
import org.ace.accounting.user.User;
import org.ace.java.web.common.ParamId;

public class AuthenticationFilter implements Filter {

	public AuthenticationFilter() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);
			String reqURI = reqt.getRequestURI();

			if (reqURI.indexOf("/index.xhtml") >= 0 || reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource") || reqURI.contains("resources")) {
				chain.doFilter(request, response);
			} else if ((ses != null && ses.getAttribute(ParamId.LOGIN_USER) != null)) {
				boolean authenticate = true;
				String[] values = reqURI.split("accounting");
				User user = (User) ses.getAttribute(ParamId.LOGIN_USER);
				if (user == null) {
					resp.sendRedirect(reqt.getContextPath() + "/index.xhtml");
				}
				if (values.length > 1) {
					for (Role role : user.getRoles()) {
						for (WebPage page : role.getWebpages()) {
							if (page.getUrl().equalsIgnoreCase(values[1]) || values[1].startsWith("/dialog")) {
								authenticate = true;
								break;
							}
						}
					}
				}

				if (authenticate) {
					chain.doFilter(request, response);
				} else {
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} else {
				resp.sendRedirect(reqt.getContextPath() + "/index.xhtml");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void destroy() {

	}
}
