package com.peterphi.std.guice.crudsample.ui.impl;

import com.google.inject.Inject;
import com.peterphi.std.guice.crudsample.db.dao.PostDao;
import com.peterphi.std.guice.crudsample.db.dao.UserDao;
import com.peterphi.std.guice.crudsample.db.entity.PostEntity;
import com.peterphi.std.guice.crudsample.guice.auth.WebappUser;
import com.peterphi.std.guice.crudsample.ui.api.PostUIService;
import com.peterphi.std.guice.database.annotation.Transactional;
import com.peterphi.std.guice.hibernate.webquery.ConstrainedResultSet;
import com.peterphi.std.guice.hibernate.webquery.ResultSetConstraint;
import com.peterphi.std.guice.hibernate.webquery.ResultSetConstraintBuilderFactory;
import com.peterphi.std.guice.restclient.exception.RestException;
import com.peterphi.std.guice.web.rest.templating.TemplateCall;
import com.peterphi.std.guice.web.rest.templating.Templater;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class PostUIServiceImpl implements PostUIService
{
	@Inject
	WebappUser user;

	@Inject
	Templater templater;

	@Inject
	PostDao dao;

	@Inject
	UserDao userDao;

	@Inject
	ResultSetConstraintBuilderFactory constraintBuilder;


	@Override
	@Transactional(readOnly = true)
	public String getAll(UriInfo info)
	{
		TemplateCall call = templater.template("posts");

		ResultSetConstraint constraint = constraintBuilder.build(info.getQueryParameters());
		ConstrainedResultSet<PostEntity> resultset = dao.findByUriQuery(constraint);
		call.set("resultset", resultset);

		return call.process();
	}


	@Override
	@Transactional(readOnly = true)
	public String get(Long id)
	{
		TemplateCall call = templater.template("post");

		final PostEntity entity = dao.getById(id);

		if (entity == null)
			throw new RestException(404, "No such entity with id");

		call.set("entity", entity);

		return call.process();
	}


	@Override
	@Transactional(readOnly = true)
	public String getEdit(Long id)
	{
		TemplateCall call = templater.template("post_edit");

		final PostEntity entity = dao.getById(id);

		if (entity == null)
			throw new RestException(404, "No such entity with id");

		call.set("entity", entity);

		return call.process();
	}


	@Override
	public String getCreate(final Long replyToId)
	{
		TemplateCall call = templater.template("post_create");

		call.set("replyToId", replyToId);

		return call.process();
	}


	@Override
	@Transactional
	public Response doCreate(final String replyToId, final String comment)
	{
		final PostEntity entity = new PostEntity();

		if (StringUtils.isNotEmpty(replyToId))
			entity.setReplyTo(dao.getById(Long.parseLong(replyToId)));

		entity.setPoster(userDao.getById(user.getId()));
		entity.setComment(comment);

		final long id = dao.save(entity);

		return Response.seeOther(URI.create("/post/" + id)).build();
	}
}
