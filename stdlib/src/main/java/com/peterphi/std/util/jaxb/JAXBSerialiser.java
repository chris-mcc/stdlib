package com.peterphi.std.util.jaxb;

import com.peterphi.std.util.DOMUtils;
import com.peterphi.std.util.jaxb.exception.JAXBRuntimeException;
import org.apache.log4j.Logger;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * A thread-safe, caching JAXB helper type. This wraps a JAXBContext and its Marshallers and Unmarshallers, allowing their
 * concurrent use<br />
 * This helper also translates the exceptions that can be thrown during a JAXB serialise/deserialise operation in a {@link
 * JAXBRuntimeException}
 */
public class JAXBSerialiser
{
	private static final Logger log = Logger.getLogger(JAXBSerialiser.class);

	private final JAXBContext context;
	private Schema schema;
	private boolean prettyOutput = false;


	/**
	 * Construct a JAXBSerialiser with a given JAXB Context Path
	 *
	 * @param contextPath
	 */
	private JAXBSerialiser(String contextPath)
	{
		try
		{
			this.context = JAXBContext.newInstance(contextPath);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Error creating JAXB Context: " + e.getMessage(), e);
		}
	}


	/**
	 * Construct a JAXBSerialiser for a given JAXBContext
	 *
	 * @param context
	 */
	private JAXBSerialiser(JAXBContext context)
	{
		this.context = context;
	}


	/**
	 * Construct a JAXBSerialiser by parsing the named classes
	 *
	 * @param classes
	 */
	private JAXBSerialiser(Class<?>... classes)
	{
		try
		{
			this.context = JAXBContext.newInstance(classes);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Error creating JAXB Context: " + e.getMessage(), e);
		}
	}


	/**
	 * Retrieve the inner JAXBContext<br/>
	 * NOTE: use of this method is strongly discouraged!
	 *
	 * @return
	 */
	public JAXBContext getContext()
	{
		return context;
	}


	/**
	 * Optionally specify the schema to use for all future serialisation/deserialisation methods
	 *
	 * @param schema
	 *
	 * @return this for method chaining
	 */
	public JAXBSerialiser setSchema(Schema schema)
	{
		this.schema = schema;

		return this;
	}


	/**
	 * Enable/Disable pretty printing
	 *
	 * @param pretty
	 *
	 * @return this for method chaining
	 */
	public JAXBSerialiser setPrettyOutput(boolean pretty)
	{
		this.prettyOutput = pretty;

		return this;
	}


	private Marshaller getMarshaller()
	{
		try
		{
			Marshaller jaxb = context.createMarshaller();

			jaxb.setSchema(schema);

			if (prettyOutput)
			{
				jaxb.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			}

			return jaxb;
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Error creating marshaller", e);
		}
	}


	private Unmarshaller getUnmarshaller()
	{
		try
		{
			Unmarshaller jaxb = context.createUnmarshaller();

			jaxb.setSchema(schema);

			return jaxb;
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Error creating unmarshaller", e);
		}
	}

	//
	//
	// Deserialisers
	//
	//


	/**
	 * Deserialise a String of XML to an Object (or JAXBElement)
	 *
	 * @param xml
	 *
	 * @return
	 */
	public Object deserialise(String xml)
	{
		return deserialise(new InputSource(new StringReader(xml)));
	}


	/**
	 * Deserialise a stream of XML to an Object (or JAXBElement)
	 *
	 * @param is
	 *
	 * @return
	 */
	public Object deserialise(final InputStream is)
	{
		return deserialise(new InputSource(is));
	}


	/**
	 * Deserialise a Reader of XML to an Object (or JAXBElement)
	 *
	 * @param reader
	 *
	 * @return
	 */
	public Object deserialise(final Reader reader)
	{
		return deserialise(new InputSource(reader));
	}


	/**
	 * Deserialise an input and cast to a particular type
	 *
	 * @param clazz
	 * @param source
	 *
	 * @return
	 */
	public <T> T deserialise(final Class<T> clazz, final InputSource source)
	{
		final Object obj = deserialise(source);

		if (clazz.isInstance(obj))
			return clazz.cast(obj);
		else
			throw new JAXBRuntimeException("XML deserialised to " + obj.getClass() + ", could not cast to the expected " + clazz);
	}


	/**
	 * Deserialise and cast to a particular type
	 *
	 * @param clazz
	 * @param xml
	 * 		a String of XML
	 *
	 * @return
	 */
	public <T> T deserialise(final Class<T> clazz, final String xml)
	{
		final Object obj = deserialise(new InputSource(new StringReader(xml)));

		if (clazz.isInstance(obj))
			return clazz.cast(obj);
		else
			throw new JAXBRuntimeException("XML deserialised to " + obj.getClass() + ", could not cast to the expected " + clazz);
	}

	//
	// Core deserialisers
	//


	/**
	 * Deserialise a File of XML to an Object (or JAXBElement)
	 *
	 * @param file
	 *
	 * @return
	 */
	public Object deserialise(final File file)
	{
		if (file == null)
			throw new IllegalArgumentException("Null argument passed to deserialise!");
		if (!file.exists())
			throw new IllegalArgumentException("File does not exist: " + file);

		final Unmarshaller unmarshaller = getUnmarshaller();

		try
		{
			final Object obj = unmarshaller.unmarshal(file);

			if (obj == null)
				throw new RuntimeException("Malformed XML from " + file);
			else
				return obj;
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Deserialisation failed: " + e.getMessage(), e);
		}
	}


	public Object deserialise(final InputSource source)
	{
		if (source == null)
			throw new IllegalArgumentException("Null argument passed to deserialise!");

		final Unmarshaller unmarshaller = getUnmarshaller();

		try
		{
			final Object obj = unmarshaller.unmarshal(source);

			if (obj == null)
				throw new RuntimeException("Malformed XML!");
			else
				return obj;
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Deserialisation failed: " + e.getMessage(), e);
		}
	}


	/**
	 * Deserialise a DOM Node to an Object (or JAXBElement)
	 *
	 * @param node
	 *
	 * @return
	 */
	public Object deserialise(final Node node)
	{
		if (node == null)
			throw new IllegalArgumentException("Null argument passed to deserialise!");

		final Unmarshaller unmarshaller = getUnmarshaller();

		try
		{
			final Object obj = unmarshaller.unmarshal(node);

			if (obj == null)
				throw new RuntimeException("Error deserialising from " + node);
			else
				return obj;
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("Deserialisation failed: " + e.getMessage(), e);
		}
	}

	//
	//
	// Serialisers
	//
	//


	/**
	 * Helper method to serialise an Object to an org.w3c.dom.Document
	 *
	 * @param obj
	 * 		the object to serialise
	 *
	 * @return a new Document containing the serialised form of the provided Object
	 */
	public Document serialiseToDocument(final Object obj)
	{
		final Document document = DOMUtils.createDocumentBuilder().newDocument();

		serialise(obj, document);

		return document;
	}


	public String serialise(final Object obj)
	{
		final Marshaller marshaller = getMarshaller();

		try
		{
			final StringWriter w = new StringWriter(1024);
			marshaller.marshal(obj, w);
			return w.toString();
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("serialisation failed: " + e.getMessage(), e);
		}
	}


	public void serialise(final Object obj, final Writer writer)
	{
		final Marshaller marshaller = getMarshaller();

		try
		{
			marshaller.marshal(obj, writer);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("serialisation failed: " + e.getMessage(), e);
		}
	}


	public void serialise(final Object obj, final OutputStream os)
	{
		final Marshaller marshaller = getMarshaller();

		try
		{
			marshaller.marshal(obj, os);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("serialisation failed: " + e.getMessage(), e);
		}
	}


	public void serialise(final Object obj, final File file)
	{
		final Marshaller marshaller = getMarshaller();

		try
		{
			marshaller.marshal(obj, file);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("serialisation failed: " + e.getMessage(), e);
		}
	}


	public void serialise(final Object obj, final Node node)
	{
		final Marshaller marshaller = getMarshaller();

		try
		{
			marshaller.marshal(obj, node);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException("serialisation failed: " + e.getMessage(), e);
		}
	}


	/**
	 * Helper method to get a JAXBSerialiser from an existing JAXBContext.<br />
	 * This is an expensive operation and so the result should ideally be cached
	 *
	 * @param context
	 * 		an existing JAXBContext
	 *
	 * @return
	 */
	public static JAXBSerialiser getInstance(JAXBContext context)
	{
		if (log.isTraceEnabled())
			log.trace("Create serialiser for context " + context);

		return new JAXBSerialiser(context);
	}


	/**
	 * Helper method to get a JAXBSerialiser for a set of classes with the underlying JAXB implementation picked using the default
	 * rules for JAXB acquisition<br />
	 * This is an expensive operation and so the result should ideally be cached
	 *
	 * @param classes
	 *
	 * @return
	 */
	public static JAXBSerialiser getInstance(Class<?>... classes)
	{
		if (log.isTraceEnabled())
			log.trace("Create serialiser for " + Arrays.asList(classes));

		return new JAXBSerialiser(classes);
	}


	/**
	 * Helper method to get a JAXBSerialiser from a JAXB Context Path (i.e. a package name or colon-delimited list of package
	 * names)  with the underlying JAXB implementation picked using the default rules for JAXB acquisition<br />
	 * This is an expensive operation and so the result should ideally be cached
	 *
	 * @param contextPath
	 * 		a package name or colon-delimited list of package names
	 *
	 * @return
	 */
	public static JAXBSerialiser getInstance(String contextPath)
	{
		if (log.isTraceEnabled())
			log.trace("Create serialiser for " + contextPath);

		return new JAXBSerialiser(contextPath);
	}


	/**
	 * Helper method to get a JAXBSerialiser that uses EclipseLink MOXy for all operations
	 *
	 * @param contextPath
	 *
	 * @return
	 */
	public static JAXBSerialiser getMoxy(String contextPath)
	{
		if (log.isTraceEnabled())
			log.trace("Create moxy serialiser for " + contextPath);

		try
		{
			JAXBContext ctx = JAXBContextFactory.createContext(contextPath, Thread.currentThread().getContextClassLoader());

			return getInstance(ctx);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException(e);
		}
	}


	/**
	 * Helper method to get a JAXBSerialiser that uses EclipseLink MOXy for all operations
	 *
	 * @param classes
	 *
	 * @return
	 */
	public static JAXBSerialiser getMoxy(Class<?>... classes)
	{
		if (log.isTraceEnabled())
			log.trace("Create moxy serialiser for " + Arrays.asList(classes));

		try
		{
			JAXBContext ctx = JAXBContextFactory.createContext(classes, null);

			return getInstance(ctx);
		}
		catch (JAXBException e)
		{
			throw new JAXBRuntimeException(e);
		}
	}
}