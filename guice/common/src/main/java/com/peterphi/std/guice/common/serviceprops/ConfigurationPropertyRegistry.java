package com.peterphi.std.guice.common.serviceprops;

import com.google.inject.Injector;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfigurationPropertyRegistry
{
	private static final Logger log = Logger.getLogger(ConfigurationPropertyRegistry.class);

	private final SortedMap<String, ConfigurationProperty> properties = new TreeMap<>();
	private final Map<Class, WeakHashMap<Object, Void>> instances = new HashMap<>();

	private final Configuration configuration;
	private final Configuration overrides;


	public ConfigurationPropertyRegistry(final Configuration configuration, final Configuration overrides)
	{
		this.configuration = configuration;
		this.overrides = overrides;
	}


	<T, O> void register(final Class<O> owner,
	                     AtomicReference<Injector> injector,
	                     String name,
	                     Class<T> type,
	                     AnnotatedElement element)
	{
		register(new ConfigurationPropertyBindingSite<>(this, injector, owner, name, type, element));
	}


	<T, O> void register(ConfigurationPropertyBindingSite<T, O> site)
	{
		synchronized (properties)
		{
			if (!properties.containsKey(site.getName()))
			{
				log.debug("Discovered new property: " + site.getName());

				properties.put(site.getName(), new ConfigurationProperty(this, configuration, overrides, site.getName()));
			}

			log.trace("Discovered new binding for property " +
			          site.getName() +
			          " of type " +
			          site.getType() +
			          " in " +
			          site.getOwner());

			properties.get(site.getName()).add(site);
		}
	}


	public ConfigurationProperty get(String name)
	{
		synchronized (properties)
		{
			return properties.get(name);
		}
	}


	public List<ConfigurationProperty> getAll()
	{
		return getAll(p -> true);
	}


	private List<ConfigurationProperty> getAll(Predicate<ConfigurationProperty> predicate)
	{
		assert (predicate != null);

		// Sort application properties, then framework properties
		// Within these groups, sort alphabetically

		Comparator<ConfigurationProperty> sort = Comparator.comparing(ConfigurationProperty:: isFrameworkProperty)
		                                                   .thenComparing(ConfigurationProperty:: getName);

		synchronized (properties)
		{
			return properties.values().stream().filter(predicate).sorted(sort).collect(Collectors.toList());
		}
	}


	public List<ConfigurationProperty> getFrameworkProperties()
	{
		return getAll(ConfigurationProperty:: isFrameworkProperty);
	}


	public List<ConfigurationProperty> getApplicationProperties()
	{
		return getAll(p -> !p.isFrameworkProperty());
	}


	/**
	 * Register an instance of a property-consuming type; the registry will use a weak reference to hold on to this instance, so
	 * that it can be discarded if it has a short lifespan
	 *
	 * @param discoveredType
	 * @param newlyConstructed
	 */
	void addInstance(final Class<?> discoveredType, final Object newlyConstructed)
	{
		WeakHashMap<Object, Void> map;

		synchronized (instances)
		{
			map = instances.get(discoveredType);

			if (map == null)
			{
				map = new WeakHashMap<>();
				instances.put(discoveredType, map);
			}
		}

		synchronized (map)
		{
			map.put(newlyConstructed, null);
		}
	}


	Iterable<Object> getInstances(final Class type)
	{
		final WeakHashMap<Object, Void> map;

		synchronized (instances)
		{
			map = instances.get(type);
		}

		if (map != null)
		{
			synchronized (map)
			{
				return new ArrayList<>(map.keySet());
			}
		}
		else
		{
			return Collections.emptyList();
		}
	}


	private static final class X
	{
		public boolean b;
		public String name;


		public X(final boolean b, final String name)
		{
			this.b = b;
			this.name = name;
		}


		public boolean isB()
		{
			return b;
		}


		public String getName()
		{
			return name;
		}


		@Override
		public String toString()
		{
			return "X{" +
			       "b=" + b +
			       ", name='" + name + '\'' +
			       '}';
		}
	}


	public static void main(String[] args) throws Exception
	{
		List<X> list = new ArrayList<>();

		list.add(new X(true, "a"));
		list.add(new X(true, "ab"));
		list.add(new X(true, "c"));
		list.add(new X(false, "a"));
		list.add(new X(false, "b"));
		list.add(new X(false, "c"));

		Collections.shuffle(list);

		list.sort(Comparator.comparing(X:: isB).thenComparing(X:: getName));

		System.out.println(list);
	}
}
