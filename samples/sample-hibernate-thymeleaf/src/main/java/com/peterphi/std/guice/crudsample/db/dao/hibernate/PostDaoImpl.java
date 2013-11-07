package com.peterphi.std.guice.crudsample.db.dao.hibernate;

import com.google.inject.Singleton;
import com.peterphi.std.guice.crudsample.db.entity.PostEntity;
import com.peterphi.std.guice.crudsample.db.dao.PostDao;
import com.peterphi.std.guice.hibernate.dao.HibernateDao;

@Singleton
public class PostDaoImpl extends HibernateDao<PostEntity, Long> implements PostDao
{
}

