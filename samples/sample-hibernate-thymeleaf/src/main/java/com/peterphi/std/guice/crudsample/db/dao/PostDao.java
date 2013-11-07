package com.peterphi.std.guice.crudsample.db.dao;

import com.google.inject.ImplementedBy;
import com.peterphi.std.guice.crudsample.db.dao.hibernate.PostDaoImpl;
import com.peterphi.std.guice.crudsample.db.entity.PostEntity;
import com.peterphi.std.guice.database.dao.Dao;

@ImplementedBy(PostDaoImpl.class)
public interface PostDao extends Dao<PostEntity, Long>
{
}
