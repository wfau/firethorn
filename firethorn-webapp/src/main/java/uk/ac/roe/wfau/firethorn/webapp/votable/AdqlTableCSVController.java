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
package uk.ac.roe.wfau.firethorn.webapp.votable;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTableLinkFactory;

/**
 * Spring Controller to generate VOTable response for an AdqlTable.
 * 
 */
@Controller
@RequestMapping(AdqlTableLinkFactory.CSV_PATH)
public class AdqlTableCSVController
extends BaseTableCSVController
    {
    /**
     * Public constructor.
     *
     */
    public AdqlTableCSVController()
        {
        super();
        }

    @Override
    public Path path()
        {
        return path(
            AdqlTableLinkFactory.CSV_PATH
            );
        }

    /**
     * VOTable GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET)
    public void CSV(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        final HttpServletResponse response
        ) throws EntityNotFoundException, IOException {
        response.setCharacterEncoding(
                "UTF-8"
                );
    	   String headerKey = "Content-Disposition";
           String headerValue = String.format("attachment; filename=\"%s\"",
                   "result.csv");
           response.setContentType("text/csv");
           response.setHeader(headerKey, headerValue);
		write(
		    response.getWriter(),
	        factories().adql().tables().select(
	            factories().adql().tables().idents().ident(
	                ident
	                )
	            )
		    );
        }
    }
