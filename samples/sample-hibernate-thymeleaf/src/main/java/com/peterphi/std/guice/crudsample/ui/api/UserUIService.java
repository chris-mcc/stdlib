package com.peterphi.std.guice.crudsample.ui.api;


import com.google.inject.ImplementedBy;
import com.peterphi.std.annotation.Doc;
import com.peterphi.std.guice.crudsample.ui.impl.UserUIServiceImpl;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@ImplementedBy(UserUIServiceImpl.class)
@Doc("Web interface for user entities")
public interface UserUIService
{
	@GET
	@Path("/users")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Search for matching entities whose properties match those provided in the query string")
	public String getAll(@Context UriInfo uriInfo);

	@GET
	@Path("/user/{id}")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Retrieve and display a particular entity")
	public String get(@PathParam("id") String id);

	@GET
	@Path("/user/{id}/edit")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Get an edit form for a particular entity")
	public String getEdit(@PathParam("id") String id);

	@GET
	@Path("/users/create")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Get a create form for a particular entity")
	public String getCreate();

	@POST
	@Path("/users/create")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Create a new entity")
	public Response doCreate(@FormParam("id") String id, @FormParam("name") String name, @FormParam("password") String password);
}
