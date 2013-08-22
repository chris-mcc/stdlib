package com.mediasmiths.std.guice.common.converter;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

class URLTypeConverter implements TypeConverter
{

	@Override
	public Object convert(String value, TypeLiteral<?> toType)
	{
		try
		{
			return new URL(value);
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException("Cannot parse URL: " + value, e);
		}
	}

}
