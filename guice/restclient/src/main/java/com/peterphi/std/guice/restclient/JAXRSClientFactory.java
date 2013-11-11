package com.peterphi.std.guice.restclient;

import com.google.inject.ImplementedBy;
import com.peterphi.std.guice.restclient.resteasy.impl.ResteasyClientFactoryImpl;

import javax.ws.rs.client.Client;
import java.net.URI;

/**
 * A factory that builds dynamic proxy clients (using JAX-RS RESTful client interfaces) for services at a known location
 */
@ImplementedBy(ResteasyClientFactoryImpl.class)
public interface JAXRSClientFactory
{
	/**
	 * Return a bare JAX-RS Client
	 *
	 * @return
	 */
	public Client client();

	/**
	 * Create a proxy client for a given interface at a fixed endpoint. Users should likely use {@link
	 * com.peterphi.std.guice.restclient.RestClientFactory#proxy(Class)} or {@link com.peterphi.std.guice.restclient.RestClientFactory#proxy(String, Class)}
	 * instead.
	 *
	 * @param iface
	 * 		the service interface class
	 * @param endpoint
	 * 		the endpoint of the service
	 * @param <T>
	 *
	 * @return
	 */
	public <T> T proxy(final Class<T> iface, final URI endpoint);
}
