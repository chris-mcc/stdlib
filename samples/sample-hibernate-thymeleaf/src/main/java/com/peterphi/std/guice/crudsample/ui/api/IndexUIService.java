package com.peterphi.std.guice.crudsample.ui.api;

import com.google.inject.ImplementedBy;
import com.peterphi.std.guice.crudsample.ui.impl.IndexUIServiceImpl;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@ImplementedBy(IndexUIServiceImpl.class)
public interface IndexUIService
{
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String getIndex();

	@POST
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public Response doLogout();


	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	public String getLogin(@FormParam("returnTo") @DefaultValue("/") String returnTo);

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	public Response doLogin(@FormParam("id") String name,
	                        @FormParam("password") String password,
	                        @FormParam("returnTo") @DefaultValue("/") String returnTo);
}
