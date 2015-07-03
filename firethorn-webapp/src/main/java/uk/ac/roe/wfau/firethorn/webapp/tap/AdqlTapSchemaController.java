/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import javax.servlet.ServletContext;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapSchemaController extends AbstractController {

	public static final String TARGET_ENTITY = "urn:adql.resource.entity";

	@Autowired
	private ServletContext servletContext;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/");
	}

	/**
	 * Get the target workspace based on the ident in the path.
	 * 
	 */
	@ModelAttribute(TARGET_ENTITY)
	public AdqlResource entity(@PathVariable("ident") final String ident)
			throws IdentifierNotFoundException {
		log.debug("entity() [{}]", ident);
		return factories().adql().resources()
				.select(factories().adql().resources().idents().ident(ident));
	}

	/**
	 * Web service method Generate the TAP_SCHEMA for this resource
	 * 
	 * @param resource
	 * @param response
	 * @throws IdentifierNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value = "generateTapSchema", method = { RequestMethod.GET })
	public void generateTapSchema(
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response)
			throws IdentifierNotFoundException, IOException, SQLException,
			ClassNotFoundException {
		
		JDBCParams params = new JDBCParams("jdbc:jtds:sqlserver://localhost:1432/FirethornUserdataSTV011317TEST", "", "", "net.sourceforge.jtds.jdbc.Driver","FirethornUserdataSTV011317TEST");
		TapSchemaGeneratorImpl generator = new TapSchemaGeneratorImpl(params, servletContext, factories(), resource, "WEB-INF/data/sqlserver_tap_schema.sql");
		generator.createTapSchema();
		
	}

}
