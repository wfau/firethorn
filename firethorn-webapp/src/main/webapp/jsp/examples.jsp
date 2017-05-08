<!DOCTYPE html
  PUBLIC "-//W3C//DTD XHTML+RDFa 1.1//EN"
  "http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd">
<html version="XHTML+RDFa 1.1" xmlns="http://www.w3.org/1999/xhtml">
<head><title>WFAU TAP Examples</title></head>


<body vocab="ivo://ivoa.net/std/DALI-examples#" class="container">


<div id="body">
	<h1>Examples for WFAU OSA TAP service</h1>

	<ul id="toc"><li>
			<a href="#QueryDates"><span class="plainmeta">Dates on which the Y band images in a strip were taken</span></a>
		</li><li>
			<a href="#QueryNullValues"><span class="plainmeta">Null and default values in the OSA</span></a>
		</li><li>
			<a href="#RegionSearch"><span class="plainmeta">RegionSearch</span></a>
		</li><li>
			<a href="#ATLASNeighbours"><span class="plainmeta">ATLAS Neighbours</span></a>
		</li></ul>

	<div id="exampleslist">
			<div typeof="example" id="QueryDates" resource="#QueryDates">
				<h2 property="name">Dates on which the Y band images in a strip were taken</h2>
				<p>A user may be interested in the dates on which the Y band images in a strip were taken. The Multiframe table in the OSA has an attribute called mjdObs, which records the Modified Julian Date of the observation. The *MergeLog and Multiframe tables are linked by having the common attribute multiframeID, which is a unique identifier for each FITS file ingested into the archive. The SQL query retrieving the desired dates here would be:</p>
				<pre class="literal-block" property="query">
				SELECT mjdObs, ra, dec
				FROM ATLASDR1.atlasMergeLog, ATLASDR1.Multiframe 
				WHERE (dec BETWEEN -22.5 AND -12.5) 
				AND (gmfID = multiframeID) AND (gmfID > 0)
				</pre>

			</div>
		
			<div typeof="example" id="QueryNullValues" resource="#QueryNullValues">
				<h2 property="name">Null and default values in the OSA</h2>
				<p> The *Source tables in the OSA merges information about detections made in the individual frame set images. A very blue object may well not be detected in a K band image, so what should be written for such an object in the column of the *Source table which records, say, the ellipicity of the K band detection? One answer would be a null value, which is a special type of entry to be included in a table if the value of a given attribute is not known (or is indeterminate or is not applicable) for a particular row. In designing the OSA we have decided not to use nulls in these cases, but to define default values for use in these situations instead: e.g. in the example above, we would set the K band ellipticity in the *Source table of a celestial object undetected in that band to be a recognisably meaningless value; in the case of floating point numbers, -0.9999995e9. Nulls and default values are semantically different: the query processor in a database management system (DBMS) recognises that a value marked as null is unknown or indeterminate and will not include it in, say, the computation of the mean value of all entries in a column, while, to the query processor, a default value is like any other value, and will include it, unless the user explicitly excludes it - e.g. by computing the mean magnitude only for those objects with magnitudes brighter than -0.9999995e9, in this case. Other default values in the OSA include -99999999 for 4- and 8-byte integer attributes, -9999 for 2-byte integers, and 0 for 1-byte (unsigned) integers. The schema browser generally indicates the default value for many of the table attributes, and they have all been chosen to lie well beyond the range of legitimate values found in the OSA, so it is simple to exclude them:</p>
				<pre class="literal-block" property="query">
				SELECT AVG(rAperMag3)
				FROM ATLASDR1.atlasSource 
				WHERE rAperMag3 > 0.0

				</pre>
			</div>
		
			<div typeof="example" id="RegionSearch" resource="#RegionSearch">
				<h2 property="name">Region Search</h2>
				<p>Use this query to search around a given position or object name:</p>
				
				<pre class="literal-block" property="query">
				SELECT  * FROM ATLASDR1.atlasSource 
				WHERE dec &gt; -2.3 and dec &lt; -2.28 and RA &gt;= 182.983 and RA &lt;= 183.01 and 
				((cx * -0.99782503392251698 + cy * -0.05229379414090411 + cz * -0.04013179253255972 ) &gt;= 0.99999995769202532)  	
				and (priOrSec &lt;= 0 OR priOrSec=frameSetID)
				</pre>
			</div>
			<div typeof="example" id="ATLASNeighbours" resource="#ATLASNeighbours">
				<h2 property="name">ATLAS Neighbours</h2>
				<p>In OSA parlance, a table of pointers that associates sources within one table (i.e. that provides a list of all nearby sources for each source within one table) is called a neighbour table, while a table of pointers that associates a set of externally catalogued sources to a table of VST sources is called a cross-neighbour table. Cross-neighbour tables are likely to be used the most, but neighbour tables have their uses (for example, you may wish to check the internal consistency of VST photometry and astrometry by selecting a set of measurements of the same sources in overlap regions of adjacent frame sets - this is most easily done using a neighbour table).

				A list of all available neighbour/cross-neighbour tables is most easily obtained as follows:</p>
				<pre class="literal-block" property="query">
				SELECT neighbourTable FROM ATLASDR1.RequiredNeighbours
				</pre>


			</div>

	</div>
</div>
</body>
</html>

