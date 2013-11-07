package com.peterphi.std.guice.crudsample.ui.api;


import com.google.inject.ImplementedBy;
import com.peterphi.std.annotation.Doc;
import com.peterphi.std.guice.crudsample.ui.impl.PostUIServiceImpl;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@ImplementedBy(PostUIServiceImpl.class)
@Doc("Web interface for post entities")
public interface PostUIService
{
	@GET
	@Path("/posts")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Search for matching entities whose properties match those provided in the query string")
	public String getAll(@Context UriInfo uriInfo);

	@GET
	@Path("/post/{id}")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Retrieve and display a particular entity")
	public String get(@PathParam("id") Long id);

	@GET
	@Path("/post/{id}/edit")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Get an edit form for a particular entity")
	public String getEdit(@PathParam("id") Long id);

	@GET
	@Path("/posts/create")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Get a create form for a particular entity")
	public String getCreate(@QueryParam("replyTo") Long replyToId);

	@POST
	@Path("/posts/create")
	@Produces(MediaType.TEXT_HTML)
	@Doc("Create a new entity")
	public Response doCreate(@FormParam("replyTo") @DefaultValue("") String replyToId, @FormParam("comment") String comment);
}
