/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.net.URL;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.DelaysClient.Param;

/**
 * These tests  no longer work - because we need firethorn to create the tables for us.
 *
 */
public class PipelineClientTestCase
extends SimpleQueryTestBase
    {
    /**
     * The test class load time.
     *
     */
    protected static long start = System.currentTimeMillis() ;

    /**
     * A shared counter for unique names.
     *
     */
    protected static long uniques = 0 ;

    /**
     * Glue for generated names.
     *
     */
    public static final String UNIQUE_NAME_GLUE = "_" ;

    /**
     * Generate a unique string.
     *
     */
    public String unique()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            String.valueOf(
                start
                )
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
        builder.append(
            String.valueOf(
                uniques++
                )
            );
        return builder.toString();
        }

    /**
     * Generate a unique string.
     *
     */
    public String unique(final String prefix)
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            prefix
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
        builder.append(
            unique()
            );
        return builder.toString();
        }

    /**
     * Single catalog.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        final PipelineClient pipeline = new PipelineClient(
            new URL(
                endpoint
                )
            );
        pipeline.execute(
            new PipelineParam()
                {
                
                @Override
                public String source()
                    {
                    return "twomass";
                    }
            
                @Override
                public String query()
                    {
                    return "" + 
                    " SELECT" +
                    "    twomass.ra," +
                    "    twomass.dec" +
                    " FROM" +
                    "    twomass_psc AS twomass" +
                    " WHERE" +
                    "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
                    " AND" +
                    "    twomass.dec BETWEEN '20.0' AND '22.9'" ;
                    }

                @Override
                public InsertClient.Param insert()
                    {
                    return new InsertClient.Param()
                        {
                        @Override
                        public String table()
                            {
                            return unique("table");
                            }
                        
                        @Override
                        public String store()
                            {
                            return "userdata";
                            }

                        @Override
                        public Integer first()
                            {
                            return null;
                            }

                        @Override
                        public Integer block()
                            {
                            return null;
                            }
                        };
                    }

                @Override
                public DelaysClient.Param delays()
                    {
                    return null;
                    }

                @Override
                public LimitsClient.Param limits()
                    {
                    return null;
                    }

                @Override
                public RownumClient.Param rows()
                    {
                    return null;
                    }
                }
            );
        }

    /**
     * Single catalog with delay.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        final PipelineClient pipeline = new PipelineClient(
            new URL(
                endpoint
                )
            );
        pipeline.execute(
            new PipelineParam()
                {
                @Override
                public String source()
                    {
                    return "twomass";
                    }
            
                @Override
                public String query()
                    {
                    return "" + 
                    " SELECT" +
                    "    twomass.ra," +
                    "    twomass.dec" +
                    " FROM" +
                    "    twomass_psc AS twomass" +
                    " WHERE" +
                    "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
                    " AND" +
                    "    twomass.dec BETWEEN '20.0' AND '22.9'" ;
                    }

                @Override
                public InsertClient.Param insert()
                    {
                    return new InsertClient.Param()
                        {
                        @Override
                        public String table()
                            {
                            return unique("table");
                            }
                        
                        @Override
                        public String store()
                            {
                            return "userdata";
                            }

                        @Override
                        public Integer first()
                            {
                            return null;
                            }

                        @Override
                        public Integer block()
                            {
                            return null;
                            }
                        };
                    }
                
                @Override
                public Param delays()
                    {
                    return new Param()
                        {
                        @Override
                        public Integer first()
                            {
                            return new Integer(100);
                            }

                        @Override
                        public Integer last()
                            {
                            return new Integer(100);
                            }

                        @Override
                        public Integer every()
                            {
                            return new Integer(5);
                            }
                        };
                    }

                @Override
                public LimitsClient.Param limits()
                    {
                    return null;
                    }

                @Override
                public RownumClient.Param rows()
                    {
                    return null;
                    }
                }
            );
        }
    }