package com.peterphi.std.guice.web.rest.jaxrs.converter;

import org.jboss.resteasy.spi.StringConverter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.ws.rs.ext.Provider;

@Provider
 class LocalDateStringConverter implements StringConverter<LocalDate>
{
	private static final DateTimeFormatter PARSER = ISODateTimeFormat.localDateParser();
	private static final DateTimeFormatter FORMATTER = ISODateTimeFormat.date();


	@Override
	public LocalDate fromString(final String str)
	{
		if (str == null)
			return null;
		else if (str.equalsIgnoreCase("today"))
			return new LocalDate();
		else
			return PARSER.parseLocalDate(str);
	}


	@Override
	public String toString(final LocalDate value)
	{
		if (value == null)
			return null;
		else
			return FORMATTER.print(value);
	}
}