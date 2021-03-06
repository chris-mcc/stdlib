package com.peterphi.std.indexservice.rest.impl.repo;

import com.google.inject.Inject;
import com.peterphi.std.guice.common.eagersingleton.annotations.EagerSingleton;
import com.peterphi.std.guice.common.shutdown.iface.ShutdownManager;
import com.peterphi.std.guice.common.shutdown.iface.StoppableService;
import com.peterphi.std.threading.Daemon;
import com.peterphi.std.threading.Timeout;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * An eager singleton worker that removes expired applications periodically
 */
@EagerSingleton
public class ExpireApplicationsWorker extends Daemon implements StoppableService
{
	private static final Logger log = Logger.getLogger(ExpireApplicationsWorker.class);

	private final AppRepo repo;

	@Inject
	public ExpireApplicationsWorker(ShutdownManager shutdownManager, AppRepo repo)
	{
		this.repo = repo;

		shutdownManager.register(this);

		startThread("IndexService-ExpireApplications");
	}

	@Override
	protected boolean shouldStartAsDaemon()
	{
		return true;
	}

	@Override
	public void run()
	{
		while (isRunning())
		{
			try
			{
				// Wait for the timeout period
				new Timeout(repo.getHeartbeatRate(), TimeUnit.MILLISECONDS).sleep();

				// Expire stale applications
				repo.expire();
			}
			catch (Exception e)
			{
				log.error("Ignoring exception in ExpireApplications thread: ", e);
			}
			catch (Error e)
			{
				log.fatal("Error in ExpireApplications thread: ", e);
				throw e;
			}
		}
	}

	@Override
	public void shutdown()
	{
		this.stopThread();
	}
}