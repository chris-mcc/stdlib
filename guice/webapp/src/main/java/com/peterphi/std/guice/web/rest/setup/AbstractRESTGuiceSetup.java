package com.peterphi.std.guice.web.rest.setup;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.peterphi.std.guice.apploader.GuiceSetup;
import com.peterphi.std.guice.web.rest.CoreRestServicesModule;
import com.peterphi.std.guice.web.rest.scoping.ServletScopingModule;
import com.peterphi.std.io.PropertyFile;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Abstract GuiceSetup implementation that registers standard functionality we offer to REST service modules
 */
public abstract class AbstractRESTGuiceSetup implements GuiceSetup
{
	private static final Logger log = Logger.getLogger(AbstractRESTGuiceSetup.class);

	/**
	 * The override for the core REST services
	 */
	public static final String DISABLE_CORE_SERVICES = "framework.restcoreservices.disabled";


	@Override
	public void registerModules(List<Module> modules, PropertyFile config)
	{
		modules.add(new ServletScopingModule());

		if (!config.getBoolean(DISABLE_CORE_SERVICES, false))
		{
			modules.add(new CoreRestServicesModule());
		}
		else {
			log.info("REST Core Services disabled by config parameter");
		}

		addModules(modules, config);
	}


	public abstract void addModules(List<Module> modules, PropertyFile config);


	@Override
	public final void injectorCreated(Injector injector)
	{
		injectorWasCreated(injector);
	}


	public abstract void injectorWasCreated(Injector injector);
}
