package com.peterphi.std.indexservice.rest.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterphi.std.indexservice.rest.iface.IndexRestService;
import com.peterphi.std.indexservice.rest.impl.repo.AppRepo;
import com.peterphi.std.indexservice.rest.impl.repo.ServiceRepo;
import com.peterphi.std.indexservice.rest.type.*;

import javax.ws.rs.PathParam;
import java.util.Date;
import java.util.List;

@Singleton
public class IndexRestServiceImpl implements IndexRestService
{
	@Inject
	protected AppRepo apps;

	@Inject
	protected ServiceRepo services;

	@Override
	public RegistrationResponse registerApplication(final RegistrationRequest request)
	{
		final String applicationId = apps.register(request);

		RegistrationResponse response = new RegistrationResponse();
		response.applicationId = applicationId;

		return response;
	}

	@Override
	public ApplicationSearchResults getAllApplications()
	{
		final List<String> applicationIds = apps.getAllIds();

		ApplicationSearchResults results = new ApplicationSearchResults();

		results.ids.addAll(applicationIds);

		return results;
	}

	@Override
	public UnregisterResponse deleteApplication(@PathParam("application_id") final String applicationId)
	{
		apps.unregister(applicationId);
		return new UnregisterResponse();
	}

	@Override
	public ServiceSearchResults getServicesForApplication(@PathParam("application_id") final String applicationId)
	{
		return renderSearchResults(apps.getServices(applicationId), false);
	}

	@Override
	public RegistrationResponse reregisterApplication(@PathParam("application_id") final String applicationId,
	                                                  final RegistrationRequest request)
	{
		apps.reregister(applicationId, request);

		RegistrationResponse response = new RegistrationResponse();
		response.applicationId = applicationId;
		return response;
	}

	@Override
	public RegistrationHeartbeatResponse heartbeatApplication(@PathParam("application_id") final String applicationId)
	{
		// Heartbeat the application (succeeds if still registered, otherwise it fails)
		final boolean success = apps.heartbeat(applicationId);

		RegistrationHeartbeatResponse response = new RegistrationHeartbeatResponse();

		response.nextHeartbeatExpectedBy = new Date(System.currentTimeMillis() + apps.getHeartbeatRate());

		if (success)
		{
			response.mustReregister = Boolean.FALSE;
		}
		else
		{
			response.mustReregister = Boolean.TRUE; // application must re-register
		}

		return response;
	}

	@Override
    /**
     * get by interface iface + opt properties or get all
     */
	public ServiceSearchResults searchForServices(final ServiceSearchRequest request)
	{
		if (request.iface != null)
		{
            return renderSearchResults(services.findByInterfaceRestrictByProperties(request.iface,request.properties), true);
		}
		else
		{
			return renderSearchResults(services.getAllServices(), true);
		}
	}

	@Override
	public ServiceSearchResults getAllServices()
	{
		return renderSearchResults(services.getAllServices(), true);
	}

	private ServiceSearchResults renderSearchResults(final List<ServiceDescription> services, boolean dropExpired)
	{
		ServiceSearchResults results = new ServiceSearchResults();


		if (dropExpired)
		{
			final long now = System.currentTimeMillis();

			for (ServiceDescription service : services)
			{
				if (service.nextHeartbeatDue.getTime() >= now)
				{
					results.services.add(service);
				}
			}
		}
		else
		{
			results.services.addAll(services);
		}

		return results;
	}
}