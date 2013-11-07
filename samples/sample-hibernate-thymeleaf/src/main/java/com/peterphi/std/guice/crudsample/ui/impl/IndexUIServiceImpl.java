package com.peterphi.std.guice.crudsample.ui.impl;

import com.google.inject.Inject;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappAuthenticationModule;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.crudsample.service.UserService;
import com.peterphi.std.guice.crudsample.ui.api.IndexUIService;
import com.peterphi.std.guice.database.annotation.Transactional;
import com.peterphi.std.guice.web.rest.templating.Templater;

import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;

public class IndexUIServiceImpl implements IndexUIService
{
	private static final int ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365;

	@Inject
	WebappUser user;

	@Inject
	Templater templater;

	@Inject
	UserService userService;

	@Inject
	HttpSession session;


	@Override
	public String getIndex()
	{
		return templater.template("index").process();
	}


	@Override
	public Response doLogout()
	{
		user.setUser(null);

		// Terminate the session
		session.invalidate();

		// Unset the Session Id cookie
		NewCookie cookie = new NewCookie(WebappAuthenticationModule.SESSION_ID_COOKIE, "none", null, null, null, 0, false);

		return Response.seeOther(URI.create("/")).cookie(cookie).build();
	}


	@Override
	public String getLogin(@FormParam("returnTo") @DefaultValue("/") final String returnTo)
	{
		return templater.template("login").set("returnTo", returnTo).process();
	}


	@Override
	@Transactional
	public Response doLogin(final String id, final String password, final String returnTo)
	{
		userService.login(user, id, password);

		final UserEntity entity = userService.getById(user.getId());

		NewCookie cookie = new NewCookie(WebappAuthenticationModule.SESSION_ID_COOKIE,
		                                 entity.getSessionId(),
		                                 null,
		                                 null,
		                                 null,
		                                 ONE_YEAR_IN_SECONDS,
		                                 false);

		return Response.seeOther(URI.create(returnTo)).cookie(cookie).build();
	}
}
