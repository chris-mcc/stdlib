package com.peterphi.std.guice.restclient;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.peterphi.std.io.PropertyFile;

import javax.ws.rs.client.WebTarget;
import java.net.URI;

/**
 * A service client for RESTful services by name (reading
 */
@Singleton
public class RestClientFactory
{
	@Inject
	JAXRSClientFactory clientFactory;


	@Inject
	@Named("service.properties")
	PropertyFile properties;


	/**
	 * Create a JAX-RS WebTarget for a given service name
	 *
	 * @param name
	 * 		the name of the service connection
	 *
	 * @return
	 */
	public WebTarget target(String name)
	{
		final URI endpoint = getEndpoint(name);

		return clientFactory.client().target(endpoint);
	}


	/**
	 * Create a proxy client for the default implementation of an interface
	 *
	 * @param iface
	 * 		the service interface class
	 * @param <T>
	 *
	 * @return
	 */
	public <T> T proxy(final Class<T> iface)
	{
		final String name = getName(iface);

		return proxy(iface, name);
	}


	/**
	 * Create a proxy client for a named implementation of an interface
	 *
	 * @param iface
	 * 		the service interface class
	 * @param name
	 * 		the name of the service connection
	 * @param <T>
	 *
	 * @return
	 */
	public <T> T proxy(final Class<T> iface, String name)
	{
		final URI endpoint = getEndpoint(name);

		return clientFactory.proxy(iface, endpoint);
	}


	/**
	 * Get the default name of the service by its interface
	 *
	 * @param iface
	 *
	 * @return
	 */
	protected String getName(Class<?> iface)
	{
		return iface.getSimpleName();
	}


	/**
	 * Retrieve the endpoint of a service by name
	 *
	 * @param name
	 *
	 * @return
	 */
	protected URI getEndpoint(String name)
	{
		final URI uri = properties.getURI("service." + name + ".endpoint", null);

		if (uri != null)
			return uri;
		else
			throw new IllegalArgumentException("No service found by name: " + name);
	}
}
