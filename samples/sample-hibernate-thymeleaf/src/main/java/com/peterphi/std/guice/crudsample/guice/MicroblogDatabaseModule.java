package com.peterphi.std.guice.crudsample.guice;

import com.peterphi.std.guice.crudsample.db.entity.PostEntity;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.hibernate.module.HibernateModule;
import org.hibernate.cfg.Configuration;

public class MicroblogDatabaseModule extends HibernateModule
{
	@Override
	protected void configure(final Configuration config)
	{
		config.addAnnotatedClass(UserEntity.class);
		config.addAnnotatedClass(PostEntity.class);
	}
}
