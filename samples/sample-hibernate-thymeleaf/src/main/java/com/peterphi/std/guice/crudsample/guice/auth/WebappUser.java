package com.peterphi.std.guice.crudsample.guice.auth;

import com.peterphi.std.guice.common.auth.iface.CurrentUser;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class WebappUser implements CurrentUser
{
	private long globalRevisionAtRefresh = Long.MIN_VALUE;

	private final ACLRefresher refresher;

	private String id = null;
	private String name = null;

	private final Set<String> groups = new HashSet<>();


	public WebappUser(ACLRefresher refresher)
	{
		this.refresher = refresher;
	}


	public void setUser(UserEntity entity)
	{
		setUser(entity, Long.MIN_VALUE);
	}


	protected void setUser(UserEntity entity, long globalRevision)
	{
		groups.clear();

		if (entity == null)
		{
			id = null;
			name = null;
			this.groups.clear();
		}
		else
		{
			id = entity.getId();
			name = entity.getName();
			// TODO read group data
		}

		// Record the time of the last refresh operation
		globalRevisionAtRefresh = globalRevision;
	}


	/**
	 * If necessary, refresh the data in this user object from the database
	 */
	private void refreshIfNecessary()
	{
		if (id == null)
			return; // no need to refresh if there's no user data

		if (refresher.getGlobalRevision() != globalRevisionAtRefresh)
		{
			refresher.refresh(this);
		}
	}


	@Override
	public boolean isAnonymous()
	{
		refreshIfNecessary();

		return id == null;
	}


	@Override
	public String getName()
	{
		return name;
	}


	public String getId()
	{
		return id;
	}


	@Override
	public String getUsername()
	{
		if (id != null)
			return id;
		else
			return null;
	}


	public boolean isAdmin()
	{
		return hasRole("admin");
	}


	@Override
	public boolean hasRole(final String s)
	{
		refreshIfNecessary();

		if (StringUtils.equalsIgnoreCase(s, "user"))
			return !isAnonymous();
		else
			return groups.contains(s);
	}
}
