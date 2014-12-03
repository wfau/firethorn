package uk.org.ogsadai.dqp.lqp.operators;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.BindingPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;

/**
 * 
 */
public class ScanBindApplyOperator extends ApplyOperator
{
    private List<BindingPredicate> mBindingPredicates;
    
    protected ScanBindApplyOperator()
    {
        mID = OperatorID.SCAN_BIND_APPLY;
    }
    
    public ScanBindApplyOperator(Operator operator,
        List<BindingPredicate> bindingPredicates)
    {
        this();
        mBindingPredicates = bindingPredicates;
        
        // set wrapped operator
        mOperator = operator;
        
        // copy annotations
        Map<String, Object> annotations = ((AbstractOperator) operator).mAnnotations;
        for (String a : annotations.keySet())
        {
            // We don't want to inherit IMPLEMENTATION annotation
            if (!a.equals(Annotation.IMPLEMENTATION))
            {
                mAnnotations.put(a, annotations.get(a));
            }
        }
        
        Set<Attribute> attrToBind = new HashSet<Attribute>();
        for (BindingPredicate bp : bindingPredicates)
            attrToBind.add(bp.getBoundAttribute());
        mAttributesToBind = attrToBind;
    }
    
    public List<BindingPredicate> getBindingPredicates()
    {
        return mBindingPredicates;
    }
    
    public int getParameterisedBranchIndex()
    {
        int leftCount = 0;
        for(Attribute a : mAttributesToBind)
            if(mLeftChildOperator.getHeading().contains(a))
                leftCount++;
            else
                break;
        
        int rightCount = 0;
        for(Attribute a : mAttributesToBind)
            if(mRightChildOperator.getHeading().contains(a))
                rightCount++;
            else
                break;
     
        if(leftCount > 0 && rightCount > 0)
        {
            // we have a problem
            throw new IllegalStateException();
        }
        else if(leftCount == 0 && rightCount == 0)
        {
            // we have a different problem
            throw new IllegalStateException();
        }
        else if(leftCount > 0)
        {
            if(leftCount < mAttributesToBind.size())
            {
                // we have a problem
                throw new IllegalStateException();
            }
            else
            {
                return 0;
            }
        }
        else
        {
            if(rightCount < mAttributesToBind.size())
            {
                // we have a problem
                throw new IllegalStateException();
            }
            else
            {
                return 1;
            }
        }
    }
    
}
