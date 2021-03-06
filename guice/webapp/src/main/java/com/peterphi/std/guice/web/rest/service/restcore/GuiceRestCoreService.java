package com.peterphi.std.guice.web.rest.service.restcore;

import com.google.inject.ImplementedBy;
import com.peterphi.std.annotation.Doc;
import com.peterphi.std.guice.restclient.annotations.FastFailServiceClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Doc("Core framework development services")
@Path("/guice/rest-core")
@ImplementedBy(GuiceRestCoreServiceImpl.class)
@FastFailServiceClient
public interface GuiceRestCoreService
{
	@Doc("Returns HTTP 200 when called")
	@GET
	@Path("/ping")
	public abstract String ping();

	@Doc("Returns the service configuration currently in memory (or fails if this functionality has been disabled)")
	@GET
	@Path("/service.properties")
	@Produces("text/plain")
	public abstract String properties() throws Exception;

	@Doc("Restarts the Guice environment within this webapp without restarting the webapp (or fails if this functionality has been disabled)")
	@GET
	@Path("/restart")
	public abstract String restart() throws Exception;
}
