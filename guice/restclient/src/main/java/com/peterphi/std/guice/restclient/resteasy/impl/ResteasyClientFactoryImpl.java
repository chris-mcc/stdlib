package com.peterphi.std.guice.restclient.resteasy.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.peterphi.std.guice.common.shutdown.iface.ShutdownManager;
import com.peterphi.std.guice.common.shutdown.iface.StoppableService;
import com.peterphi.std.guice.restclient.JAXRSClientFactory;
import com.peterphi.std.threading.Timeout;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of a JAX-RS Dynamic Proxy HTTP Client Factory that uses RestEasy
 */
@Singleton
public class ResteasyClientFactoryImpl implements JAXRSClientFactory, StoppableService
{

	@Inject(optional = true)
	@Named("jaxrs.connection.timeout")
	Timeout connectionTimeout = new Timeout(20, TimeUnit.SECONDS);

	@Inject(optional = true)
	@Named("jaxrs.socket.timeout")
	Timeout socketTimeout = new Timeout(5, TimeUnit.MINUTES);

	@Inject(optional = true)
	@Named("jaxrs.nokeepalive")
	boolean noKeepalive = true;


	private final PoolingClientConnectionManager connectionManager;
	private final ResteasyClient client;


	@Inject
	public ResteasyClientFactoryImpl(final ShutdownManager manager,
	                                 final ResteasyClientErrorInterceptor errorInterceptor,
	                                 final JAXBContextResolver jaxbContextResolver)
	{
		this.connectionManager = new PoolingClientConnectionManager();

		this.client = createClient(errorInterceptor, jaxbContextResolver);

		manager.register(this);
	}


	private ResteasyClient createClient(final ResteasyClientErrorInterceptor errorInterceptor,
	                                    final JAXBContextResolver jaxbContextResolver)
	{
		final DefaultHttpClient client;
		{
			client = new DefaultHttpClient(connectionManager);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, (int) connectionTimeout.getMilliseconds());
			HttpConnectionParams.setSoTimeout(params, (int) socketTimeout.getMilliseconds());

			// Prohibit keepalive if desired
			if (noKeepalive)
			{
				client.setReuseStrategy(new NoConnectionReuseStrategy());
			}

			// TODO add authentication
		}

		ResteasyClientBuilder builder = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient4Engine(client));

		builder.register(jaxbContextResolver);
		builder.register(errorInterceptor);

		return builder.build();
	}


	@Override
	public void shutdown()
	{
		if (client != null)
		{
			if (!client.isClosed())
				client.close();
		}

		connectionManager.shutdown();
	}


	@Override
	public ResteasyClient client()
	{
		return client;
	}


	@Override
	public <T> T proxy(final Class<T> iface, final URI endpoint)
	{
		return client.target(endpoint).proxy(iface);
	}
}
