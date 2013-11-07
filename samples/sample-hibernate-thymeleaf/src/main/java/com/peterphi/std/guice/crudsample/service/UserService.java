package com.peterphi.std.guice.crudsample.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterphi.std.crypto.BCrypt;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.database.annotation.Transactional;

import java.util.regex.Pattern;

@Singleton
public class UserService
{
	/**
	 * Valid usernames start with a letter and then continue wih letters, numbers and underscores
	 */
	private static final Pattern pattern = Pattern.compile("^[A-Za-z][A-Za-z0-9_]+$");

	@Inject
	UserDao dao;


	@Transactional
	public void login(WebappUser user, String id, String password)
	{
		if (validate(id))
		{
			final UserEntity entity = dao.getById(normalise(id));

			if (entity != null)
				if (BCrypt.checkpw(password, entity.getPasswordHash()))
				{
					user.setUser(entity);
					return;
				}
		}

		throw new IllegalArgumentException("Invalid username and/or password!");
	}


	@Transactional
	public String register(String id, String name, String password)
	{
		if (!validate(id))
			throw new IllegalArgumentException("Invalid username: " + id);

		final UserEntity entity = new UserEntity();

		entity.setId(normalise(id));
		entity.setName(name);
		entity.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

		dao.save(entity);

		return entity.getId();
	}


	protected String normalise(String id)
	{
		return id.toLowerCase();
	}


	protected boolean validate(String id)
	{
		return pattern.matcher(id).matches();
	}


	public UserEntity getById(final String id)
	{
		return dao.getById(id);
	}
}
