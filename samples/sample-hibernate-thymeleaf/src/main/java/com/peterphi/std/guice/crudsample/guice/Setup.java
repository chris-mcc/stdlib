package com.peterphi.std.guice.crudsample.guice;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.peterphi.std.guice.crudsample.guice.auth.WebappAuthenticationModule;
import com.peterphi.std.guice.thymeleaf.ThymeleafModule;
import com.peterphi.std.guice.web.rest.setup.AbstractRESTGuiceSetup;
import com.peterphi.std.io.PropertyFile;

import java.util.List;

public class Setup extends AbstractRESTGuiceSetup
{
	@Override
	public void injectorWasCreated(Injector injector)
	{

	}


	@Override
	public void addModules(List<Module> modules, PropertyFile config)
	{
		modules.add(new MicroblogModule());
		modules.add(new MicroblogDatabaseModule());

		modules.add(new ThymeleafModule());
		modules.add(new WebappAuthenticationModule());
	}
}
