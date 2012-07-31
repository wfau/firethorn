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
package uk.ac.roe.wfau.firethorn.experiment ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Date;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon ;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase ;

import org.metagrid.gatekeeper.node.Node;
import org.metagrid.gatekeeper.node.NodeImpl;

import org.metagrid.gatekeeper.node.ident.IdentBuilder;

import org.metagrid.gatekeeper.node.property.DateProperty;
import org.metagrid.gatekeeper.node.property.StringProperty;
import org.metagrid.gatekeeper.node.property.ReadOnlyProperty;

/**
 *
 */
@Slf4j
public class WidgeonNodeImpl
extends NodeImpl
implements WidgeonNode
    {

    /**
     * Reference to our Widgeon.
     * 
     */
    private Widgeon widgeon;

    /**
     * Access to our Widgeon.
     * 
     */
    public Widgeon widgeon()
        {
        return this.widgeon;
        }

    /**
     * Public constructor.
     * 
     */
    public WidgeonNodeImpl(IdentBuilder<String, Widgeon> builder, Widgeon widgeon)
        {
        log.debug("WidgeonNode(IdentBuilder<String, Widgeon>, Widgeon)");
        log.debug("  Widgeon [{}]", widgeon);
        this.widgeon = widgeon;
        this.builder = builder;

        //
        // Object name.
        this.init(
            new StringProperty(
                this,
                WidgeonNode.NAME_PROPERTY_URI
                ){
                @Override
                protected String getString()
                    {
                    return widgeon().name();
                    }
                @Override
                protected void setString(String value)
                    {
                    widgeon().name(value);
                    }
                }
            );
        //
        // Created date.
        this.init(
            ReadOnlyProperty.wrap(
                new DateProperty(
                    this,
                    WidgeonNode.CREATED_PROPERTY_URI
                    ){
                    @Override
                    protected Date getDate()
                        {
                        return widgeon().created();
                        }
                    @Override
                    protected void setDate(Date value)
                        {
                        }
                    }
                )
            );
        //
        // Modified date.
        this.init(
            ReadOnlyProperty.wrap(
                new DateProperty(
                    this,
                    WidgeonNode.MODIFIED_PROPERTY_URI
                    ){
                    @Override
                    protected Date getDate()
                        {
                        return widgeon().modified();
                        }
                    @Override
                    protected void setDate(Date value)
                        {
                        }
                    }
                )
            );
        }

    /**
     * Our ident parser.
     * 
     */
    private IdentBuilder<String, Widgeon> builder;

    @Override
    public URI ident()
        {
        log.debug("WidgeonNodeImpl.ident()");
        log.debug("  Widgeon [{}]", widgeon);
        return builder.build(
            widgeon
            );
        }

    @Override
    public URI type()
        {
        log.debug("WidgeonNodeImpl.type()");
        log.debug("  Widgeon [{}]", widgeon);
        return WidgeonNode.NODE_TYPE_URI;
        }

    }

