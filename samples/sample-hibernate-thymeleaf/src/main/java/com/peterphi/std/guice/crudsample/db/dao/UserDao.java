package com.peterphi.std.guice.crudsample.db.dao;

import com.google.inject.ImplementedBy;
import com.peterphi.std.guice.crudsample.db.dao.hibernate.UserDaoImpl;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.database.dao.Dao;

@ImplementedBy(UserDaoImpl.class)
public interface UserDao extends Dao<UserEntity, String>
{
	public void populateWebappUserFromSessionId(final WebappUser user, final String sessionCookie);
}
