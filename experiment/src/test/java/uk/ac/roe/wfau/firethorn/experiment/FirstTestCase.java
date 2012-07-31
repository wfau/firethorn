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

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon ;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase ;
import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

import org.metagrid.gatekeeper.node.Node;
import org.metagrid.gatekeeper.node.NodeServer;
import org.metagrid.gatekeeper.node.NodeServerImpl;

import org.metagrid.gatekeeper.node.ident.IdentBuilder;
import org.metagrid.gatekeeper.common.io.test.StringResource;
import org.metagrid.gatekeeper.common.xml.reader.node.XMLNodeReader;
import org.metagrid.gatekeeper.common.xml.reader.node.XMLNodeReaderImpl;
import org.metagrid.gatekeeper.common.xml.writer.node.XMLNodeWriter;
import org.metagrid.gatekeeper.common.xml.writer.node.XMLNodeWriterImpl;

/**
 *
 */
@Slf4j
public class FirstTestCase
extends TestBase
    {

    /**
     * Our test Womble.
     *
     */
    @Autowired
    private Womble womble ;

    /**
     * Access to our test Womble.
     *
     */
    public Womble womble()
        {
        return this.womble;
        }

    public void flush()
        {
        log.debug("flush()");
        womble().hibernate().flush();
        }

    /**
     * Our test Widgeon.
     *
     */
    private WidgeonBase base ;

    protected WidgeonBase base()
        {
        return this.base;
        }

    @Before
    public void before()
    throws Exception
        {
        base = womble().widgeons().create(
            this.unique(
                "base"
                ),
            URI.create(
                "ivo://org.astrogrid.test/test-data"
                )
            );
        }

//  @Test
    public void test001()
    throws Exception
        {
        //
        // Create view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Create base catalog, schema and table.
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    ).tables().create(
                        "table-A"
                        )
            );
        //
        // Select table view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    ).schemas().select(
                        "schema-A"
                        ).tables().select(
                            "table-A"
                            )
            );
        //
        // Create base column.
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().select(
                    "schema-A"
                    ).tables().select(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );
        //
        // Select column view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    ).schemas().select(
                        "schema-A"
                        ).tables().select(
                            "table-A"
                            ).columns().select(
                                "column-A"
                                )
            );
        }

    /**
     * Helper class to generate identifiers.
     * 
     */
    public static class WidgeonIdentBuilder
    implements IdentBuilder<String, Widgeon>
        {
        /**
         * Our base URI.
         * 
         */
        private URI base ;

        /**
         * Public constructor.
         * 
         */
        public WidgeonIdentBuilder(URI base)
            {
            this.base = base ;
            log.debug("WidgeonIdentBuilder(URI)");
            log.debug("  Base [{}]", base);
            }
        
        @Override
        public URI build(Widgeon widgeon)
            {
            log.debug("WidgeonIdentBuilder.build(Widgeon)");
            log.debug("  Base    [{}]", base);
            log.debug("  Widgeon [{}]", widgeon);
            URI result = base.resolve(
                widgeon.ident().toString()
                );
            log.debug("  Result  [{}]", result);
            return result ;
            }

        @Override
        public String parse(URI uri)
            {
            log.debug("WidgeonIdentBuilder.parse(URI)");
            log.debug("  Base [{}]", base);
            log.debug("  URI  [{}]", uri);
            String result = base.relativize(uri).getPath();
            log.debug("  Result [{}]", result);
            return result ;
            }
        }

//  @Test
    public void test002()
    throws Exception
        {
        //
        // Create our view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Wrap our view as a Node.
        WidgeonNode node = new WidgeonNodeImpl(
            new WidgeonIdentBuilder(
                URI.create(
                    "urn://test-widgeons/"
                    )
                ),
            base().views().select(
                "view-A"
                )
            );

        //
        // Create our target resource.
        StringResource resource = new StringResource(
            "test data"
            );
        //
        // Create our Node writer.
        XMLNodeWriter<WidgeonNode> writer = new XMLNodeWriterImpl<WidgeonNode>();
        //
        // Write the node ....
        writer.write(
            resource,
            node
            );

        log.debug("----");
        log.debug(resource.data());
        log.debug("----");

        }


    /**
     * A WidgeonNode server.
     * 
     */
    public class WidgeonNodeServerImpl
    extends NodeServerImpl<WidgeonNode>
    implements NodeServer<WidgeonNode>
        {

        /**
         * Public constructor.
         * 
         */
        public WidgeonNodeServerImpl(IdentBuilder<String, Widgeon> builder)
            {
            super(WidgeonNode.NODE_TYPE_URI);
            this.builder = builder;
            }

        /**
         * Our ident builder.
         * 
         */
        private IdentBuilder<String, Widgeon> builder;

        @Override
        protected WidgeonNode create(URI ident, URI type)
            {
            log.debug("WidgeonNodeServerImpl.create(URI, URI)");
            log.debug("  Ident [{}]", ident);
            log.debug("  Type  [{}]", type);
            try {
                return new WidgeonNodeImpl(
                    builder,
                    womble().widgeons().create(
                        unique(
                            "frog"
                            ),
                        URI.create(
                            "ivo://org.astrogrid.test/test-data"
                            )
                        )
                    );
                }
            catch (Exception ouch)
                {
                log.debug("Ouch []", ouch);
                return null ;
                }
            }

        @Override
        protected WidgeonNode select(URI ident, URI type)
            {
            log.debug("WidgeonNodeServerImpl.select(URI, URI)");
            log.debug("  Ident [{}]", ident);
            log.debug("  Type  [{}]", type);
            try {
                return new WidgeonNodeImpl(
                    builder,
                    womble().widgeons().select(
                        builder.parse(
                            ident
                            )
                        )
                    );
                }
            catch (Exception ouch)
                {
                log.debug("Ouch []", ouch);
                return null ;
                }
            }

        @Override
        protected WidgeonNode update(URI ident, URI type)
            {
            log.debug("WidgeonNodeServerImpl.update(URI, URI)");
            log.debug("  Ident [{}]", ident);
            log.debug("  Type  [{}]", type);
            // Mismatch between different models.
            // FireThorn doesn't distinguish between read and read/write objects.
            try {
                return new WidgeonNodeImpl(
                    builder,
                    womble().widgeons().select(
                        builder.parse(
                            ident
                            )
                        )
                    );
                }
            catch (Exception ouch)
                {
                log.debug("Ouch []", ouch);
                return null ;
                }
            }
        }

    @Test
    public void test003()
    throws Exception
        {
        //
        // Create our Widgeon NodeServer.
        final NodeServer<WidgeonNode> server = new WidgeonNodeServerImpl(
            new WidgeonIdentBuilder(
                URI.create(
                    "urn://test-widgeons/"
                    )
                )
            );
        //
        // Create our XMLNodeReader.
        final XMLNodeReader<WidgeonNode> creator = new XMLNodeReaderImpl<WidgeonNode>(
            server.creator()
            );
        //
        // Parse some XML to create a WidgeonNode.
        final WidgeonNode created = creator.read(
            new StringResource(
                "<node xmlns='urn:metagrid' type='urn:uk.ac.roe.wfau.firethorn.widgeon'>"
              + "  <properties>"
              + "    <property type='urn:uk.ac.roe.wfau.firethorn.name'>Albert Augustus Charles Emmanuel</property>"
              + "  </properties>"
              + "</node>"
                ).reader()
            );

        //
        // Check the Node name.
        assertEquals(
            "Albert Augustus Charles Emmanuel",
            created.properties().get(
                WidgeonNode.NAME_PROPERTY_URI
                ).value()
            );

        //
        // Check the Widgeon name.
        assertEquals(
            "Albert Augustus Charles Emmanuel",
            created.widgeon().name()
            );

        //
        // Create our response.
        StringResource response = new StringResource(
            "response"
            );
        //
        // Create our Node writer.
        XMLNodeWriter<WidgeonNode> writer = new XMLNodeWriterImpl<WidgeonNode>();
        //
        // Write the node ....
        writer.write(
            response,
            created
            );

        log.debug("----");
        log.debug(response.data());
        log.debug("----");

/*
 *
    <zdef1128698001:node
        xmlns=""
        xmlns:zdef1128698001="urn:metagrid"
        ident="urn://test-widgeons/2"
        type="urn:uk.ac.roe.wfau.firethorn.widgeon"
        >
        <properties xmlns="urn:metagrid">
            <property type="urn:uk.ac.roe.wfau.firethorn.created">2012-07-31T19:54:15.452+0100</property>
            <property type="urn:uk.ac.roe.wfau.firethorn.modified">2012-07-31T19:54:15.453+0100</property>
            <property type="urn:uk.ac.roe.wfau.firethorn.name">Albert Augustus Charles Emmanuel</property>
        </properties>
    </zdef1128698001:node> 

 *
 */

                
        }
    }

