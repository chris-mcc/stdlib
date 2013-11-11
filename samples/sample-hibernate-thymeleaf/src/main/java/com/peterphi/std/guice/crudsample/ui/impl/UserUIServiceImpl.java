package com.peterphi.std.guice.crudsample.ui.impl;

import com.google.inject.Inject;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.crudsample.db.entity.UserEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.crudsample.service.UserService;
import com.peterphi.std.guice.crudsample.ui.api.UserUIService;
import com.peterphi.std.guice.database.annotation.Transactional;
import com.peterphi.std.guice.hibernate.webquery.ConstrainedResultSet;
import com.peterphi.std.guice.hibernate.webquery.ResultSetConstraint;
import com.peterphi.std.guice.hibernate.webquery.ResultSetConstraintBuilderFactory;
import com.peterphi.std.guice.restclient.exception.RestException;
import com.peterphi.std.guice.web.rest.templating.TemplateCall;
import com.peterphi.std.guice.web.rest.templating.Templater;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserUIServiceImpl implements UserUIService
{
	@Inject
	WebappUser user;

	@Inject
	Templater templater;

	@Inject
	UserDao dao;

	@Inject
	ResultSetConstraintBuilderFactory constraintBuilder;

	@Inject
	UserService userService;


	@Override
	@Transactional(readOnly = true)
	public String getAll(UriInfo info)
	{
		TemplateCall call = templater.template("users");

		ResultSetConstraint constraint = constraintBuilder.build(info.getQueryParameters());
		ConstrainedResultSet<UserEntity> resultset = dao.findByUriQuery(constraint);
		call.set("resultset", resultset);

		return call.process();
	}


	@Override
	@Transactional(readOnly = true)
	public String get(String id)
	{
		TemplateCall call = templater.template("user");

		final UserEntity entity = dao.getById(id);

		if (entity == null)
			throw new RestException(404, "No such entity with id");

		call.set("entity", entity);

		return call.process();
	}


	@Override
	@Transactional(readOnly = true)
	public String getEdit(String id)
	{
		TemplateCall call = templater.template("user_edit");

		final UserEntity entity = dao.getById(id);

		if (entity == null)
			throw new RestException(404, "No such entity with id");

		call.set("entity", entity);

		return call.process();
	}


	@Override
	public String getChangePassword()
	{
		TemplateCall call = templater.template("user_changepassword");

		return call.process();
	}


	@Override
	public Response doChangePassword(String id, String password)
	{
		userService.changePassword(id, password);

		return Response.seeOther(URI.create("/")).build();
	}


	@Override
	public String getCreate()
	{
		TemplateCall call = templater.template("user_create");

		final UserEntity entity = new UserEntity();

		call.set("entity", entity);

		return call.process();
	}


	@Override
	public Response doCreate(final String id, final String name, final String password)
	{
		System.out.println("id=" + id + ",name=" + name + ",password=" + password);
		final String normalisedUserId = userService.register(id, name, password);

		return Response.seeOther(URI.create("/user/" + normalisedUserId)).build();
	}
}
