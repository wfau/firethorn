package uk.org.ogsadai.dqp.firethorn;

import junit.framework.TestCase;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;

public class TAPMetadataCollectorTest extends TestCase
{
    
    public void testUKIDSS() throws Exception
    {
        String tapService = "http://wfaudata.roe.ac.uk/ukidssDR6-dsa/TAP/";
        String table = "lasSource";
        TAPService service = new TAPService(tapService);
        TAPMetadataCollector collector = new TAPMetadataCollector();
        
        double numRows = collector.getNumRows(service, table);
        collector.createHistograms(service, table, "dec", 10, false, .1);
        AttributeStatistics stats = 
                collector.createScalarAttributeStatistics(
                        service, table, "dec", numRows, .1);
        service.release();
        System.out.println(stats);
    }
    
    public void testGAVO() throws Exception
    {
        String tapService = "http://dc.zah.uni-heidelberg.de/__system__/tap/run/tap/";
        TAPService service = new TAPService(tapService);
        
        String table = "ohmaser.masers";
        TAPMetadataCollector collector = new TAPMetadataCollector();
        
        collector.createHistograms(service, table, "measure_no", 10, true, 1);
        
        double numRows = collector.getNumRows(service, table);
        AttributeStatistics stats = 
                collector.createScalarAttributeStatistics(
                        service, table, "measure_no", numRows, .1);
        service.release();
        System.out.println(stats);
    }

    public void testCADC() throws Exception
    {
        String tapService = "http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/tap/";
        TAPService service = new TAPService(tapService);

        String table = "caom.Observation";
        TAPMetadataCollector collector = new TAPMetadataCollector();

        double numRows = collector.getNumRows(service, table);
        AttributeStatistics stats = 
                collector.createHistograms(
                        service, table, "obsID", 10, false, .1);
        stats = collector.createScalarAttributeStatistics(
              service, table, "telescope_name", numRows, .1);
        service.release();
        System.out.println(stats);
    }
}
