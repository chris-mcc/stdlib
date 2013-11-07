package com.peterphi.std.guice.crudsample.guice;

import com.google.inject.AbstractModule;
import com.peterphi.std.guice.crudsample.ui.api.IndexUIService;
import com.peterphi.std.guice.crudsample.ui.api.PostUIService;
import com.peterphi.std.guice.crudsample.ui.api.UserUIService;
import com.peterphi.std.guice.serviceregistry.rest.RestResourceRegistry;

public class MicroblogModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		RestResourceRegistry.register(IndexUIService.class);
		RestResourceRegistry.register(UserUIService.class);
		RestResourceRegistry.register(PostUIService.class);
	}
}
