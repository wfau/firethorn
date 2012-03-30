package uws.service;

import uws.job.JobOwner;

public interface BackupManager {

	public int[] saveAll();

	public int[] restoreAll();

	public int[] saveOwner(final JobOwner owner);

}
