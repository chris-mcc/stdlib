package com.peterphi.std.guice.crudsample.guice.auth;

import com.peterphi.std.guice.common.auth.annotations.AuthConstraint;
import com.peterphi.std.guice.common.auth.iface.AccessRefuser;
import com.peterphi.std.guice.common.auth.iface.CurrentUser;
import com.peterphi.std.guice.web.HttpCallContext;
import com.peterphi.std.guice.web.rest.jaxrs.exception.LiteralRestResponseException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class WebappAccessRefuser implements AccessRefuser
{
	private String currentURL;


	@Override
	public Throwable refuse(final AuthConstraint constraint, final CurrentUser user)
	{
		if (user.isAnonymous())
		{
			final String currentURL = getCurrentURL();

			final URI uri = UriBuilder.fromPath("/login")
			                          .queryParam("returnTo", currentURL)
			                          .queryParam("message",
			                                      "You must log in order to view this page")
			                          .build();

			return new LiteralRestResponseException(Response.seeOther(uri).build());
		}
		else
		{
			return new AccessRefusedException("Access denied by rule: " + constraint.comment());
		}
	}


	public String getCurrentURL()
	{
		final HttpCallContext ctx = HttpCallContext.peek();

		if (ctx == null)
			return "/"; // No idea what the current page is
		else if (!StringUtils.equalsIgnoreCase("GET", ctx.getRequest().getMethod()))
			return "/"; // The request isn't a GET, don't try to return by a GET (URL may be invalid)

		final HttpServletRequest request = ctx.getRequest();

		final String uri = request.getServletPath();
		final String qs = request.getQueryString();

		if (qs == null)
			return uri;
		else
			return uri + "?" + qs;
	}
}
