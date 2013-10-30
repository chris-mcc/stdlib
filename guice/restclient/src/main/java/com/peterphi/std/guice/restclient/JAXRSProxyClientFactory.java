package com.peterphi.std.guice.restclient;

import com.google.inject.ImplementedBy;
import com.peterphi.std.guice.restclient.resteasy.impl.ResteasyClientFactoryImpl;

import java.net.URI;

/**
 * A factory that builds dynamic proxy clients (using JAX-RS RESTful client interfaces) for services at a known location
 */
@ImplementedBy(ResteasyClientFactoryImpl.class)
public interface JAXRSProxyClientFactory
{
	/**
	 * Create a dynamic proxy client for the desired service, setting the base endpoint
	 *
	 * @param iface
	 * 		the service interface
	 * @param endpoint
	 * 		a valid URI to use as a base point for the provided service interface
	 * @param <T>
	 * 		the type of the service
	 *
	 * @return a dynamic proxy for the service
	 */
	public <T> T createClient(final Class<T> iface, final URI endpoint);

	/**
	 * Create a dynamic proxy client for the desired service, setting the base endpoint
	 *
	 * @param iface
	 * 		the service interface
	 * @param endpoint
	 * 		a valid URI to use as a base point for the provided service interface
	 * @param <T>
	 * 		the type of the service
	 *
	 * @return a dynamic proxy for the service
	 *
	 * @throws IllegalArgumentException
	 * 		if <code>endpoint</code> cannot be parsed as a valid URI
	 */
	public <T> T createClient(final Class<T> iface, final String endpoint);

	/**
	 * Create a dynamic proxy client for the desired service, setting the base endpoint & setting up HTTP Basic auth
	 *
	 * @param iface
	 * 		the service interface
	 * @param endpoint
	 * 		a valid URI to use as a base point for the provided service interface
	 * @param username
	 * 		the username to use
	 * @param password
	 * 		the password to use
	 * @param <T>
	 * 		the type of the service
	 *
	 * @return a dynamic proxy for the service
	 *
	 * @throws IllegalArgumentException
	 * 		if <code>endpoint</code> cannot be parsed as a valid URI
	 */
	public <T> T createClientWithPasswordAuth(final Class<T> iface,
	                                          final URI endpoint,
	                                          final String username,
	                                          final String password);
}