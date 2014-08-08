package com.peterphi.std.guice.common.serviceprops;

import com.peterphi.std.annotation.Doc;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.AnnotatedElement;

public class ConfigurationPropertyBindingSite<T>
{
	private Class owner;
	private String name;
	private Class<T> type;
	private AnnotatedElement element;


	public ConfigurationPropertyBindingSite(final Class owner,
	                                        final String name,
	                                        final Class<T> type,
	                                        final AnnotatedElement element)
	{
		if (owner == null)
			throw new IllegalArgumentException("Binding owner must not be null!");

		this.owner = owner;
		this.name = name;
		this.type = type;
		this.element = element;
	}


	public Class getOwner()
	{
		return owner;
	}


	public String getName()
	{
		return this.name;
	}


	public Class<T> getType()
	{
		return type;
	}


	public boolean isDeprecated()
	{
		return element.isAnnotationPresent(Deprecated.class);
	}


	/**
	 * Get a description (from a @Doc annotation, if one is present)
	 *
	 * @return
	 */
	public String getDescription()
	{
		final Doc doc = element.getAnnotation(Doc.class);

		if (doc != null)
			return StringUtils.join(doc.value(), "\n");
		else
			return null;
	}


	public String[] getHrefs()
	{
		final Doc doc = element.getAnnotation(Doc.class);

		if (doc != null && doc.href() != null && doc.href().length > 0)
			return doc.href();
		else
			return null;
	}


	@Override
	public String toString()
	{
		return "BindingSite{" +
		       "owner=" + owner +
		       ", name='" + name + '\'' +
		       ", type=" + type +
		       ", element=" + element +
		       '}';
	}
}
