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
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus ;

import org.metagrid.gatekeeper.node.Node;
import org.metagrid.gatekeeper.node.NodeImpl;

import org.metagrid.gatekeeper.node.ident.IdentBuilder;

import org.metagrid.gatekeeper.node.property.DateProperty;
import org.metagrid.gatekeeper.node.property.EnumProperty;
import org.metagrid.gatekeeper.node.property.StringProperty;
import org.metagrid.gatekeeper.node.property.ReadOnlyProperty;

/**
 *
 */
@Slf4j
public class CatalogNodeImpl
extends NodeImpl
implements CatalogNode
    {

    /**
     * Reference to our Catalog.
     * 
     */
    private WidgeonBase.Catalog catalog;

    /**
     * Access to our Catalog.
     * 
     */
    public WidgeonBase.Catalog catalog()
        {
        return this.catalog;
        }

    /**
     * Public constructor.
     * 
     */
    public CatalogNodeImpl(IdentBuilder<String, WidgeonBase.Catalog> builder, WidgeonBase.Catalog catalog)
        {
        log.debug("CatalogNodeImpl(IdentBuilder<String, Widgeon.Catalog>, Widgeon.Catalog)");
        log.debug("  Catalog [{}]", catalog);
        this.catalog = catalog;
        this.builder = builder;

        //
        // Catalog name.
        this.init(
            new StringProperty(
                this,
                WidgeonNode.NAME_PROPERTY_URI
                ){
                @Override
                protected String getString()
                    {
                    return catalog().name();
                    }
                @Override
                protected void setString(String value)
                    {
                    catalog().name(value);
                    }
                }
            );

        //
        // Widgeon status.
        this.init(
            new EnumProperty<WidgeonStatus.Status>(
                this,
                WidgeonNode.STATUS_PROPERTY_URI
                ){
                @Override
                protected WidgeonStatus.Status getEnum()
                    {
                    return catalog().status();
                    }
                @Override
                protected void setEnum(String value)
                    {
                    catalog().status(
                        WidgeonStatus.Status.valueOf(
                            value
                            )
                        );
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
                        return catalog().created();
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
                        return catalog().modified();
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
    private IdentBuilder<String, WidgeonBase.Catalog> builder;

    @Override
    public URI ident()
        {
        log.debug("CatalogNodeImpl.ident()");
        log.debug("  Catalog [{}]", catalog);
        return builder.build(
            catalog
            );
        }

    @Override
    public URI type()
        {
        log.debug("CatalogNodeImpl.type()");
        log.debug("  Catalog [{}]", catalog);
        return CatalogNode.NODE_TYPE_URI;
        }
    }

