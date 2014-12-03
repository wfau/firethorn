// Copyright (c) The University of Edinburgh, 2008-2010.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.lqp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A class representing attribute sources of a predicate.
 * 
 * @author The OGSA-DAI Project Team
 */
public class PredicateSources
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2010.";

    /** Type of match. */
    private MatchType mSourcesMatch;
    /** A set of left hand side attributes. */
    private Set<Attribute> mLHSAttributes;
    /** A set of right hand side attributes. */
    private Set<Attribute> mRHSAttributes;
    /** A set of left hand side source operators. */
    private Set<Operator> mLHSSources;
    /** A set of right hand side source operators. */ 
    private Set<Operator> mRHSSources;
    /** Ambiguous attribute. */
    private Attribute mAmbiguousAttribute;
    /** Missing attribute/ */
    private Attribute mMissingAttribute;

    /**
     * Finds predicate sources for a given predicate and a collection of source
     * operators.
     * 
     * @param predicate
     *            predicate to analyse
     * @param sources
     *            a collection of source operators
     * @return results of analysis
     */
    public static PredicateSources getMatch(Predicate predicate,
        Collection<Operator> sources)
    {
        PredicateSources pSources = new PredicateSources();
        pSources.mLHSAttributes = predicate.getLHSAttributes();
        pSources.mRHSAttributes = predicate.getRHSAttributes();

        pSources.mLHSSources = new HashSet<Operator>();
        pSources.mRHSSources = new HashSet<Operator>();

        for(Attribute a : pSources.mLHSAttributes)
        {
            int match = 0;
            for(Operator o : sources)
            {
                if(o.getHeading().contains(a))
                {
                    pSources.mLHSSources.add(o);
                    match++;
                }
            }
            if(match > 1)
                pSources.mAmbiguousAttribute = a;
            else if(match == 0)
                pSources.mMissingAttribute = a;
        }

        for(Attribute a : pSources.mRHSAttributes)
        {
            int match = 0;
            for(Operator o : sources)
            {
                if(o.getHeading().contains(a))
                {
                    pSources.mRHSSources.add(o);
                    match++;
                }
            }
            if(match > 1)
                pSources.mAmbiguousAttribute = a;
            else if(match == 0)
                pSources.mMissingAttribute = a;
        }

        // ambiguous match
        if (pSources.mAmbiguousAttribute != null)
        {
            pSources.mSourcesMatch = MatchType.AMBIGUOUS;
        }
        // match not found
        else if (pSources.mMissingAttribute != null)
        {
            pSources.mSourcesMatch = MatchType.NOT_FOUND;
        }
        // no attributes
        else if (pSources.mLHSAttributes.size() == 0
            && pSources.mRHSAttributes.size() == 0)
        {
            pSources.mSourcesMatch = MatchType.NO_ATTRIBUTES;
        }
        // constant single source
        else if ((pSources.mLHSAttributes.size() == 0 && pSources.mRHSSources.size() == 1)
            || (pSources.mRHSAttributes.size() == 0 && pSources.mLHSSources.size() == 1))
        {
            pSources.mSourcesMatch = MatchType.CONST_1_SOURCE;
        }
        // constant multiple sources
        else if ((pSources.mLHSAttributes.size() == 0 && pSources.mRHSSources.size() > 1)
            || (pSources.mRHSAttributes.size() == 0 && pSources.mLHSSources.size() > 1))
        {
            pSources.mSourcesMatch = MatchType.CONST_N_SOURCES;
        }
        // both sides same source
        else if (pSources.mLHSSources.size() == 1
            && pSources.mLHSSources.equals(pSources.mRHSSources))
        {
            pSources.mSourcesMatch = MatchType.BOTH_SIDES_SAME_SOURCE;
        }
        // both sides single source
        else if (pSources.mLHSSources.size() == 1
            && pSources.mRHSSources.size() == 1
            && !pSources.mLHSSources.equals(pSources.mRHSSources))
        {
            pSources.mSourcesMatch = MatchType.BOTH_SIDES_1_SOURCE;
        }
        // both sides many sources
        else if (pSources.mLHSSources.size() > 1
            || pSources.mRHSSources.size() > 1)
        {
            pSources.mSourcesMatch = MatchType.BOTH_SIDES_N_SOURCES;
        }
        return pSources;
    }

    /**
     * Gets source operator of attributes.
     *  
     * @return operator.
     */
    public Operator getSource()
    {
        if(mLHSSources.size() == 1)
            return mLHSSources.iterator().next();
        else if(mRHSSources.size() == 1)
            return mRHSSources.iterator().next();
        else
            throw new IllegalStateException();
    }
    
    /**
     * Gets source operator of left hand side attributes.
     * 
     * @return operator.
     */
    public Operator getLHSSource()
    {
        if(mLHSSources.size() == 1)
            return mLHSSources.iterator().next();
        else
            throw new IllegalStateException();
    }

    /**
     * Gets source operator of right hand side attributes.
     * 
     * @return operator.
     */
    public Operator getRHSSource()
    {
        if(mRHSSources.size() == 1)
            return mRHSSources.iterator().next();
        else
            throw new IllegalStateException();
    }

    /**
     * Gets a set of source operators of left hand side attributes.
     * 
     * @return operator.
     */
    public Set<Operator> getLHSSourceSet()
    {
        return mLHSSources;
    }

    /**
     * Gets a set of source operators of right hand side attributes.
     * 
     * @return operator.
     */
    public Set<Operator> getRHSSourceSet()
    {
        return mRHSSources;
    }

    /**
     * Gets ambiguous attribute.
     * 
     * @return operator.
     */
    public Attribute getAmbiguousAttribute()
    {
        return mAmbiguousAttribute;
    }

    /**
     * Gets missing attributes.
     * 
     * @return operator.
     */
    public Attribute getMissingAttribute()
    {
        return mMissingAttribute;
    }
    
    /**
     * Gets source match type.
     * 
     * @return operator.
     */
    public MatchType getMatchType()
    {
        return mSourcesMatch;
    }

    /**
     * Predicate sources match type ennumeration.
     * 
     * @author The OGSA-DAI Project Team
     */
    enum MatchType
    {
        AMBIGUOUS, 
        NOT_FOUND, 
        CONST_1_SOURCE, 
        CONST_N_SOURCES, 
        BOTH_SIDES_1_SOURCE, 
        BOTH_SIDES_N_SOURCES, 
        BOTH_SIDES_SAME_SOURCE, 
        NO_ATTRIBUTES;
    }

}
