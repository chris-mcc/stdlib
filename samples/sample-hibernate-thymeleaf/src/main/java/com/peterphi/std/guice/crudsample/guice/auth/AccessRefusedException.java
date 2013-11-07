package com.peterphi.std.guice.crudsample.guice.auth;

import com.peterphi.std.guice.restclient.exception.RestException;

public class AccessRefusedException extends RestException
{
	public AccessRefusedException(final String msg)
	{
		super(403, msg);
	}
}
