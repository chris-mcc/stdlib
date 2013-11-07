package com.peterphi.std.guice.crudsample.guice.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.peterphi.std.guice.common.auth.AuthConstraintInterceptorModule;
import com.peterphi.std.guice.common.auth.iface.AccessRefuser;
import com.peterphi.std.guice.common.auth.iface.CurrentUser;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.web.rest.scoping.SessionScoped;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebappAuthenticationModule extends AbstractModule
{
	private static final Logger log = Logger.getLogger(WebappAuthenticationModule.class);
	public static final String SESSION_ID_COOKIE = "_sessionKey";


	@Override
	protected void configure()
	{
		install(new AuthConstraintInterceptorModule());

		bind(CurrentUser.class).to(WebappUser.class);

		bind(AccessRefuser.class).to(WebappAccessRefuser.class);
	}


	/**
	 * Return the WebappUser object for this http request
	 *
	 * @param refresher
	 * @param request
	 * @param dao
	 *
	 * @return
	 */
	@Provides
	@SessionScoped
	public WebappUser getCurrentUser(ACLRefresher refresher, HttpServletRequest request, UserDao dao)
	{
		final WebappUser user = new WebappUser(refresher);

		final HttpSession session = request.getSession(true);
		// Store the user in the session so it's available to Thymeleaf
		session.setAttribute("user", user);

		// Try to log the user back in if they have a valid session cookie
		final String sessionCookie = getCookieValue(request, SESSION_ID_COOKIE);

		if (sessionCookie != null)
		{
			// We can't start a transaction from within this method, so we pass over to the DAO to populate the WebappUser object
			dao.populateWebappUserFromSessionId(user, sessionCookie);
		}

		return user;
	}


	private String getCookieValue(HttpServletRequest request, String name)
	{
		if (request.getCookies() != null)
			for (Cookie cookie : request.getCookies())
			{
				if (cookie.getName().equals(name))
				{
					return cookie.getValue();
				}
			}

		return null;
	}
}
