package uws.job.manager;

/*
 * This file is part of UWSLibrary.
 * 
 * UWSLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UWSLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UWSLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import uws.UWSException;

import uws.job.AbstractJob;
import uws.job.ErrorType;
import uws.job.ExecutionPhase;

/**
 * <p>Default implementation of the ExecutionManager interface.</p>
 * 
 * <p>This manager does not have a queue. That is to say that all jobs are always immediately starting.
 * Consequently this manager is just user to gather all running jobs.</p>
 *
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 06/2011
 */
public class DefaultExecutionManager<J extends AbstractJob> implements ExecutionManager<J> {
	private static final long serialVersionUID = 1L;

	/** List of running jobs. */
	protected transient Map<String, J> runningJobs;


	/* ************ */
	/* CONSTRUCTORS */
	/* ************ */
	/**
	 * Builds an execution manager without queue.
	 * 
	 * @see #sync()
	 */
	public DefaultExecutionManager(){
		sync();
	}

	public synchronized void sync(){
		if (runningJobs == null)
			runningJobs = new LinkedHashMap<String, J>();
	}

	public final Iterator<J> getRunningJobs(){
		if (runningJobs == null)
			sync();

		return runningJobs.values().iterator();
	}

	public final int getNbRunningJobs(){
		if (runningJobs == null)
			sync();

		return runningJobs.size();
	}

	/**
	 * Always returns a Null Iterator (iterator whose next() returns <i>null</i> and hasNext() returns <i>false</i>).
	 * 
	 * @see uws.job.manager.ExecutionManager#getQueuedJobs()
	 */
	public Iterator<J> getQueuedJobs(){
		return new Iterator<J>() {
			@Override
			public boolean hasNext() { return false; }

			@Override
			public J next() { return null; }

			@Override
			public void remove() { ; }
		};
	}

	/**
	 * Always returns 0.
	 * 
	 * @see uws.job.manager.ExecutionManager#getNbQueuedJobs()
	 */
	public int getNbQueuedJobs(){
		return 0;
	}

	public void setNoQueue() { ; }

	public boolean hasQueue(){ return false; }

	public void refresh() throws UWSException {
		if (runningJobs == null)
			sync();
	}

	public synchronized ExecutionPhase execute(J jobToExecute) throws UWSException {
		if (jobToExecute == null)
			return null;

		// Refresh the list of running jobs before all:
		try{
			refresh();
		}catch(UWSException ue){
			ue.printStackTrace();
		}

		// If the job is already running, ensure it is in the list of running jobs:
		if (jobToExecute.isRunning())
			runningJobs.put(jobToExecute.getJobId(), jobToExecute);

		// If the job is already finished, ensure it is not any more in the list of running jobs:
		else if (jobToExecute.isFinished()){
			runningJobs.remove(jobToExecute);
			throw new UWSException(UWSException.BAD_REQUEST, "[Start a job] The job \""+jobToExecute.getJobId()+"\" has already been executed. It has finished with the phase \""+jobToExecute.getPhase()+"\" !", ErrorType.TRANSIENT);

			// Otherwise start it:
		} else {
			jobToExecute.start(false);
			runningJobs.put(jobToExecute.getJobId(), jobToExecute);
		}

		return jobToExecute.getPhase();
	}

	public boolean isReadyForExecution(J jobToExecute) {
		if (runningJobs == null)
			sync();

		return jobToExecute != null && !jobToExecute.isFinished();
	}

	public synchronized void update(J job) throws UWSException {
		if (runningJobs == null)
			sync();

		if (job == null || job.isFinished())
			return;

		if (job.isRunning())
			runningJobs.put(job.getJobId(), job);

		refresh();
	}

	public synchronized void remove(J jobToRemove) throws UWSException {
		if (runningJobs == null)
			sync();

		if (jobToRemove == null)
			return;

		runningJobs.remove(jobToRemove.getJobId());

		refresh();
	}
}
