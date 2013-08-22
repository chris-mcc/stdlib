package com.mediasmiths.std.guice.web.servlet;

import com.mediasmiths.std.guice.apploader.GuiceApplication;
import com.mediasmiths.std.guice.apploader.impl.GuiceRegistry;
import com.mediasmiths.std.guice.serviceregistry.ApplicationContextNameRegistry;
import com.mediasmiths.std.guice.web.HttpCallContext;
import org.apache.log4j.MDC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Basic Guice-aware servlet that is configured at invocation time and restarted using {@link GuiceRegistry}<br />
 * Feeds Servlet lifecycle events back to {@link GuiceRegistry} to perform orderly shutdown of services<br />
 * Overrides {@link HttpServlet#service} to perform lazy configuration. See the <code>doService</code> method if you need to override <code>service</code>
 */
public abstract class GuiceServlet extends HttpServlet implements GuiceApplication
{
	private static final long serialVersionUID = 1L;

	private AtomicBoolean ready = new AtomicBoolean(false);

	@Override
	protected final void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		final HttpCallContext ctx = HttpCallContext.set(req, resp, getServletContext());

		try
		{
			// Share the call id to log4j
			MDC.put("call.id", ctx.getLogId());

			// If necessary set up Guice
			if (!ready.get())
			{
				ApplicationContextNameRegistry.setContextName(getServletContext().getContextPath());

				// Register as a durable application
				GuiceRegistry.register(this, true);
			}

			// Make the call
			doService(req, resp);
		}
		finally
		{
			HttpCallContext.clear();
			MDC.clear();
		}
	}

	/**
	 * Calls {@link HttpServlet#service} should a subclass need to implement that method
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doService(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{
		super.service(req, resp);
	}

	@Override
	public final void configured()
	{
		ready.set(true);
	}

	@Override
	public final void destroy()
	{
		ready.set(false);

		super.destroy();

		// Feed shutdown to the GuiceRegistry
		GuiceRegistry.stop();
	}

	@Override
	public abstract void stopping();
}
