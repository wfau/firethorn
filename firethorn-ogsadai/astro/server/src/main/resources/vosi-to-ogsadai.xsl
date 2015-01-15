<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
   xmlns:vr='http://www.ivoa.net/xml/VOResource/v1.0'
   xmlns:vs='http://www.ivoa.net/xml/VODataService/v1.0'
   xmlns:tab='urn:astrogrid:schema:TableMetadata'
   xmlns='http://ogsadai.org.uk/namespaces/2005/10/properties'
   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
   exclude-result-prefixes="vr vs tab xsi"
   xsi:schemaLocation='      http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd
      urn:astrogrid:schema:TableMetadata Tables.xsd'>
<!-- Stylesheet for conversion of VOSI table metadata into OGSA-DAI metadata -->      
<xsl:output method="xml" indent="yes" />

<xsl:template match="/"> 
	<xsl:element name="databaseSchema" namespace="http://ogsadai.org.uk/namespaces/2005/10/properties">
	<xsl:element name="logicalSchema">
	<xsl:for-each select="/tab:tables/table">
		<table>
			<xsl:attribute name="name">
				<xsl:value-of select="name"/>
			</xsl:attribute>
			<!-- 
			<xsl:attribute name="schema">
				<xsl:text>null</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="catalog">
				<xsl:text>DATABASE_NAME</xsl:text>
			</xsl:attribute>
			 -->
		<!-- ############# Begin Column translation ################# -->
		<xsl:for-each select="child::column"> 
			<column>
				<xsl:attribute name="name">
					<xsl:value-of select="name"/>
				</xsl:attribute>
				<xsl:attribute name="fullName">
					<xsl:value-of select="description"/>
				</xsl:attribute>
				<xsl:attribute name="length">
					<xsl:text>-1</xsl:text> 
				</xsl:attribute>
				<xsl:attribute name="nullable">
					<xsl:text>true</xsl:text> 
				</xsl:attribute>
				<xsl:attribute name="default">
					<xsl:text>null</xsl:text> 
				</xsl:attribute>
				<xsl:attribute name="position">
					<xsl:number/>  
				</xsl:attribute>					
				<xsl:element name="sqlJavaTypeID">
					 <xsl:choose> 
					 	<xsl:when test="dataType = 'bit'"> 
					 		<xsl:text>-7</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'tinyint'"> 
					 		<xsl:text>-6</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'smallint'"> 
							<xsl:text>5</xsl:text>
					 	</xsl:when>
                        <xsl:when test="dataType = 'short'"> 
                            <xsl:text>5</xsl:text>
                        </xsl:when>
					 	<xsl:when test="dataType = 'int'"> 
							<xsl:text>4</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'bigint'"> 
							<xsl:text>-5</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'long'"> 
							<xsl:text>-5</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'float'"> 
							<xsl:text>6</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'real'"> 
							<xsl:text>7</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'double'"> 
							<xsl:text>8</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'numeric'"> 
							<xsl:text>2</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'decimal'"> 
							<xsl:text>3</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'char'"> 
							<xsl:text>1</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'varchar'"> 
							<xsl:text>12</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'string'"> 
							<xsl:text>12</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'longvarchar'"> 
							<xsl:text>-1</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'date'"> 
							<xsl:text>91</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'time'"> 
							<xsl:text>92</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'timestamp'"> 
							<xsl:text>93</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'binary'"> 
					 		<xsl:text>-2</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'varbinary'"> 
					 		<xsl:text>-3</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'longvarbinary'"> 
							<xsl:text>-4</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'null'"> 
							<xsl:text>0</xsl:text>
					 	</xsl:when>				 	
					 	<xsl:when test="dataType = 'other'"> 
							<xsl:text>1111</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'JAVA_OBJECT'"> 
							<xsl:text>2000</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'distinct'"> 
							<xsl:text>2001</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'struct'"> 
							<xsl:text>2002</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'array'"> 
							<xsl:text>2003</xsl:text>
					 	</xsl:when>				 	
					 	<xsl:when test="dataType = 'blob'"> 
							<xsl:text>2004</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'clob'"> 
							<xsl:text>2005</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'boolean'"> 
							<xsl:text>16</xsl:text>
					 	</xsl:when>				 	
					 </xsl:choose>
				</xsl:element> 
				<xsl:element name="tupleTypeID">
					 <xsl:choose> 
					 	<xsl:when test="dataType = 'object'"> 
					 		<xsl:text>1</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'boolean'"> 
							<xsl:text>2</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'float'"> 
							<xsl:text>3</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'int'"> 
							<xsl:text>4</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'long'"> 
							<xsl:text>5</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'varchar'"> 
							<xsl:text>6</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'string'"> 
							<xsl:text>6</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'char'"> 
							<xsl:text>7</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'double'"> 
							<xsl:text>8</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'short'"> 
							<xsl:text>9</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'bytearray'"> 
							<xsl:text>10</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'file'"> 
							<xsl:text>11</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'date'"> 
							<xsl:text>12</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'time'"> 
							<xsl:text>13</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'timestamp'"> 
							<xsl:text>14</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'blob'"> 
							<xsl:text>15</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'clob'"> 
							<xsl:text>16</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'bigdecimal'"> 
							<xsl:text>17</xsl:text>
					 	</xsl:when>
					 	<xsl:when test="dataType = 'null'"> 
							<xsl:text>18</xsl:text>
					 	</xsl:when>				 	
					 </xsl:choose> 
				</xsl:element>
				<xsl:element name="sqlTypeName">
					<xsl:value-of select="dataType"/>  
				</xsl:element>
				
			</column>
		 	</xsl:for-each>
       	       		<!-- ############# End Column translation ################# -->
       	       		
       	       		</table>
       	       	</xsl:for-each>
       	       	<!-- ############# End Table translation ################# -->
       	       	
       	       	</xsl:element>       	
       	</xsl:element>
</xsl:template>  

</xsl:stylesheet> 