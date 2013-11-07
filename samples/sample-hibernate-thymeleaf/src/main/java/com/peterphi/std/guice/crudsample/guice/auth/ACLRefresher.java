package com.peterphi.std.guice.crudsample.guice.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.database.annotation.Transactional;

@Singleton
public class ACLRefresher
{
	private long revision = 0;

	@Inject
	UserDao dao;


	/**
	 * Request a refresh of a WebappUser object; a new read-only transaction will be created for this method call if one does not
	 * already exist
	 *
	 * @param user
	 */
	public synchronized void refresh(WebappUser user)
	{
		final long retrievedRev = getGlobalRevision();

		// Don't refresh unless there's a user
		if (user.getId() != null)
		{
			setUser(user, user.getId());
		}
		else
		{
			user.setUser(null, retrievedRev);
		}
	}


	@Transactional(readOnly = true)
	void setUser(WebappUser user, String userId)
	{
		final UserEntity entity = dao.getById(userId);
		user.setUser(entity);
	}


	/**
	 * Increment the global revision, forcing a global refresh of ACLs
	 */
	public synchronized void forceRefresh()
	{
		revision++;
	}


	public synchronized long getGlobalRevision()
	{
		return revision;
	}
}
