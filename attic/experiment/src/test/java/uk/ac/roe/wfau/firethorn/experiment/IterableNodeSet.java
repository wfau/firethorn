/*
 * <meta:header>
 *     <meta:licence>
 *         Copyright (C) 2012 by Wizzard Solutions Ltd, wizzard@metagrid.co.uk
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *     </meta:licence>
 *     <svn:header>
 *         $LastChangedRevision: 337 $
 *         $LastChangedDate: 2010-05-18 17:04:01 +0100 (Tue, 18 May 2010) $
 *         $LastChangedBy: zarquan $
 *     </svn:header>
 * </meta:header>
 *
 */
package uk.ac.roe.wfau.firethorn.experiment;

import org.metagrid.gatekeeper.node.Node;

public interface IterableNodeSet<Inner extends Node>
extends Node
	{

	public Iterable<Inner> iter();

		
	}
