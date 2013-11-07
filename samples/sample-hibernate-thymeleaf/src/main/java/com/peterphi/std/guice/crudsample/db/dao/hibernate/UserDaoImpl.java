package com.peterphi.std.guice.crudsample.db.dao.hibernate;

import com.google.inject.Singleton;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.database.annotation.Transactional;
import com.peterphi.std.guice.hibernate.dao.HibernateDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Singleton
public class UserDaoImpl extends HibernateDao<UserEntity, String> implements UserDao
{
	@Override
	@Transactional(readOnly = true)
	public void populateWebappUserFromSessionId(final WebappUser user, final String sessionCookie)
	{
		final UserEntity entity = getBySessionId(sessionCookie);

		if (entity != null)
		{
			user.setUser(entity);
		}
	}


	UserEntity getBySessionId(final String sessionId)
	{
		final Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("sessionId", sessionId));
		criteria.setMaxResults(1);

		return uniqueResult(criteria);
	}
}

