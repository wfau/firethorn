package tap;

/*
 * This file is part of TAPLibrary.
 * 
 * TAPLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TAPLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with TAPLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.util.Collection;

import tap.formatter.OutputFormat;
import tap.metadata.TAPMetadata;

import uws.service.UserIdentifier;

public interface ServiceConnection<R> {

	public static enum LimitUnit {
		rows, bytes;
	}

	public static enum LogType {
		INFO, WARNING, ERROR;
	}

	public String getProviderName();

	public String getProviderDescription();

	public boolean isAvailable();

	public String getAvailability();

	public int[] getRetentionPeriod();

	public int[] getExecutionDuration();

	public int[] getOutputLimit();

	public LimitUnit[] getOutputLimitType();

	public UserIdentifier getUserIdentifier();

	public boolean uploadEnabled();

	public int[] getUploadLimit();

	public LimitUnit[] getUploadLimitType();

	public String getUploadDirectory();

	public TAPMetadata getTAPMetadata();

	public Collection<String> getCoordinateSystems();

	public TAPFactory<R> getFactory();

	public OutputFormat<R>[] getOutputFormats();

	public OutputFormat<R> getOutputFormat(final String mimeOrAlias);

	public void deleteResults(ADQLExecutor<R> job) throws TAPException;

	public void log(final LogType type, final String message);

	public void log(final Throwable error);

}
