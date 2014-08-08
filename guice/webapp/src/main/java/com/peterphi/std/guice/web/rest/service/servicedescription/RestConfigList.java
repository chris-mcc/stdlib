package com.peterphi.std.guice.web.rest.service.servicedescription;

import com.google.inject.ImplementedBy;
import com.peterphi.std.annotation.Doc;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Doc("Lists the configuration properties in use by this webapp")
@Path("/list/config")
@ImplementedBy(RestConfigListImpl.class)
public interface RestConfigList
{
	@Doc("Lists all config")
	@GET
	@Produces("text/html")
	@Path("/")
	public String index() throws Exception;

	@Doc("Reconfigures an existing config value")
	@POST
	@Produces("text/html")
	@Path("/reconfigure")
	public String setProperty(@FormParam("key") String name, @FormParam("value") String value);

	@Doc("Validates a potential config value change")
	@POST
	@Produces("application/json")
	@Path("/validate")
	public boolean validateProperty(@FormParam("key") String name, @FormParam("value") String value);
}
