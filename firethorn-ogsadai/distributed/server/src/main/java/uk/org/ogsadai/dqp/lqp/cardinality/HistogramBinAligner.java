package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistogramBinAligner
{
    static public HistogramBasedAttributeStatistics[] alignBins(
        HistogramBasedAttributeStatistics stats0,
        HistogramBasedAttributeStatistics stats1)
    {
        List<AttributeHistogramRange> alignedRanges = 
            new LinkedList<AttributeHistogramRange>();
        
        List<AttributeHistogramRange> histogram0Ranges = 
            getRanges(stats0);
        List<AttributeHistogramRange> histogram1Ranges = 
            getRanges(stats1);
        
        // TODO: May need to sort the ranges if they are no initially sorted
        
        while( !histogram0Ranges.isEmpty() && !histogram1Ranges.isEmpty())
        {
            AttributeHistogramRange range0 = histogram0Ranges.get(0);
            AttributeHistogramRange range1 = histogram1Ranges.get(0);
            AttributeHistogramRange split[];
            
            switch( compareRanges(range0, range1))
            {
                case SAME:
                    // Bins are identical
                    alignedRanges.add(range0);
                    histogram0Ranges.remove(0);
                    histogram1Ranges.remove(0);
                    break;
                    
                case BEFORE:
                    alignedRanges.add(range0);
                    histogram0Ranges.remove(0);
                    break;
                    
                case AFTER:
                    alignedRanges.add(range1);
                    histogram1Ranges.remove(0);
                    break;
                    
                case STARTS_FIRST:
                    split = splitRange(range0, range1.getMin());
                    alignedRanges.add(split[0]);
                    histogram0Ranges.remove(0);
                    histogram0Ranges.add(0, split[1]);
                    break;
                    
                case STARTS_SECOND:
                    split = splitRange(range1, range0.getMin());
                    alignedRanges.add(split[0]);
                    histogram1Ranges.remove(0);
                    histogram1Ranges.add(0, split[1]);
                    break;
                    
                case ENDS_FIRST:
                    split = splitRange(range1, 
                        new AttributeHistogramBinEndpoint(
                            range0.getMax().getPoint(),
                            !range0.getMax().getIsInclusive()));
                    alignedRanges.add(split[0]);
                    histogram0Ranges.remove(0);
                    histogram1Ranges.remove(0);
                    histogram1Ranges.add(0, split[1]);
                    break;
                    
                case ENDS_SECOND:
                    split = splitRange(range0, 
                        new AttributeHistogramBinEndpoint(
                            range1.getMax().getPoint(),
                            !range1.getMax().getIsInclusive()));
                    alignedRanges.add(split[0]);
                    histogram0Ranges.remove(0);
                    histogram0Ranges.add(0, split[1]);
                    histogram1Ranges.remove(0);
                    break;
            }
        }
        
        // At most one side will be non-empty
        alignedRanges.addAll(histogram0Ranges);
        alignedRanges.addAll(histogram1Ranges);
        
        // Now we need to populate the data in the bins
        HistogramBasedAttributeStatistics[] stats = {stats0, stats1};
        ArrayList<List<AttributeHistogramRange>> ranges = 
             new ArrayList<List<AttributeHistogramRange>>(2);
        ranges.add(alignedRanges);
        ranges.add(alignedRanges);
        
        HistogramBasedAttributeStatistics[] result = 
            new HistogramBasedAttributeStatistics[2];
        
        for (int i=0; i<2; ++i)
        {
            result[i] = new HistogramBasedAttributeStatistics();
            
            for ( AttributeHistogramRange range : ranges.get(i))
            {
                double numRows = 0;
                double numValues = 0;
                
                for( AttributeHistogramBin oldBin : stats[i].getBins())
                {
                    AttributeHistogramBin subBin = 
                        generateSubBin(oldBin, range);
                    
                    numRows += subBin.getNumRows();
                    numValues += subBin.getNumValues();
                }
                result[i].addBin(
                    new AttributeHistogramBin(range, numRows, numValues));
            }

        }
        
        return result;
    }
    
    /**
     * Generates a new bin that contains the statistics for the portion of
     * an existing bin that falls within a new range.
     * 
     * @param bin    existing bin
     * @param range  range for new bin
     * 
     * @return a new bin defined over the given range that contains statistics
     * for portion of the existing bin that lies in that range.
     */
    static public AttributeHistogramBin generateSubBin(
        AttributeHistogramBin bin,
        AttributeHistogramRange range)
    {
        switch( compareRanges(bin.getRange(), range) )
        {
        case AFTER:
        case BEFORE:
            return new AttributeHistogramBin(range, 0, 0);
            
        case SAME:
            return new AttributeHistogramBin(
                range, bin.getNumRows(), bin.getNumValues());
            
        default:
            
            double numRows;
            double numValues;
            
            // Handle single point range
            if (range.getMin().getPoint() == range.getMax().getPoint())
            {
                if (bin.isEmpty())
                {
                    numRows = 0;
                    numValues = 0;
                }
                else
                {
                    // assume this single point range is one of the values
                    numValues = 1;
                    numRows = bin.getNumRows()/bin.getNumValues();
                }
            }
            else
            {
                double min = Math.max(bin.getRange().getMin().getPoint(), 
                                      range.getMin().getPoint());
                double max = Math.min(bin.getRange().getMax().getPoint(),
                                      range.getMax().getPoint());
                
                double factor = 
                    (max-min)/
                    (bin.getRange().getMax().getPoint() - 
                     bin.getRange().getMin().getPoint());
                             
                // Handle single point values
                if (min==max) factor = 1;
                
                numRows = factor * bin.getNumRows();
                numValues = factor * bin.getNumValues();
            }
            return new AttributeHistogramBin(range, numRows, numValues);
        }
    }

    
    private static List<AttributeHistogramRange> getRanges(
        HistogramBasedAttributeStatistics stats0)
    {
        List<AttributeHistogramRange> result = 
            new LinkedList<AttributeHistogramRange>();
        for (AttributeHistogramBin bin : stats0.getBins())
        {
            result.add(bin.getRange());
        }
        return result;
    }
    
    /**
     * Splits a bin range into two bin ranges.
     * 
     * @param bin range   
     *           bin to split
     * @param secondBinMin  
     *           new minimum endpoint of the second bin
     * @return
     */
    private static AttributeHistogramRange[] splitRange(
        AttributeHistogramRange range,
        AttributeHistogramBinEndpoint secondBinMin)
    {
        AttributeHistogramRange[] result = new AttributeHistogramRange[2];
        result[0] = new AttributeHistogramRange(
            range.getMin(),
            new AttributeHistogramBinEndpoint(
                secondBinMin.getPoint(), !secondBinMin.getIsInclusive()));
        result[1] = new AttributeHistogramRange(
            secondBinMin,
            range.getMax());
        return result;
    }
        
    
    private enum RangeMatch { BEFORE, AFTER, SAME, STARTS_FIRST, STARTS_SECOND, ENDS_FIRST, ENDS_SECOND };

    
    static private RangeMatch compareRanges(AttributeHistogramRange range0, AttributeHistogramRange range1)
    {
        if (range0.equals(range1))
        {
            return RangeMatch.SAME;
        }
        
        if (range0.getMax().getPoint() < range1.getMin().getPoint())
        {
            return RangeMatch.BEFORE;
        }
        
        if (range0.getMax().getPoint() == range1.getMin().getPoint() && 
            range0.getMax().getIsInclusive() != range1.getMin().getIsInclusive())
        {
            return RangeMatch.BEFORE;
        }
        
        if (range0.getMax().getPoint() == range1.getMin().getPoint() && 
            (!range0.getMax().getIsInclusive() || !range1.getMin().getIsInclusive()))
        {
            return RangeMatch.BEFORE;
        }

        if (range1.getMax().getPoint() < range0.getMin().getPoint())
        {
            return RangeMatch.AFTER;
        }
        
        if (range1.getMax().getPoint() == range0.getMin().getPoint() && 
            (!range1.getMax().getIsInclusive() || !range0.getMin().getIsInclusive()))
        {
            return RangeMatch.AFTER;
        }
        
        // The bins overlap to some extent
        
        int compMins = compareMins(range0.getMin(), range1.getMin());
        
        if (compMins == -1)
        {
            return RangeMatch.STARTS_FIRST;
        }
        else if (compMins == 1)
        {
            return RangeMatch.STARTS_SECOND;
        }
        else // comp == 0
        {
            int compMaxs = compareMaxs(range0.getMax(), range1.getMax());
            
            if (compMaxs == -1)
            {
                return RangeMatch.ENDS_FIRST;
            }
            else if (compMaxs == 1)
            {
                return RangeMatch.ENDS_SECOND;
            }
            else
            {
                return RangeMatch.SAME;
            }
        }
    }
    
    static private int compareMins(
        AttributeHistogramBinEndpoint point0,
        AttributeHistogramBinEndpoint point1)
    {
        if (point0.getPoint() < point1.getPoint())
        {
            return -1;
        }
        if (point0.getPoint() > point1.getPoint())
        {
            return 1;
        }
        if (point0.getIsInclusive() && !point1.getIsInclusive())
        {
            return -1;
        }
        if (!point0.getIsInclusive() && point1.getIsInclusive())
        {
            return 1;
        }
        return 0;
    }
    
    static private int compareMaxs(
        AttributeHistogramBinEndpoint point0,
        AttributeHistogramBinEndpoint point1)
    {
        if (point0.getPoint() < point1.getPoint())
        {
            return -1;
        }
        if (point0.getPoint() > point1.getPoint())
        {
            return 1;
        }
        if (!point0.getIsInclusive() && point1.getIsInclusive())
        {
            return -1;
        }
        if (point0.getIsInclusive() && !point1.getIsInclusive())
        {
            return 1;
        }
        return 0;
    }


}
