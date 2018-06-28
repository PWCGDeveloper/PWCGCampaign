package pwcg.campaign.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class TargetTypeAttackGenerator
{
    private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    private List <TacticalTarget> preferredTargetTypes = new ArrayList<TacticalTarget>();
    private List <TacticalTarget> longRangeTargetTypes = new ArrayList<TacticalTarget>();

    public TargetTypeAttackGenerator(TargetTypeAvailabilityInputs targetTypeAvailabilityInputs)
    {
        this.targetTypeAvailabilityInputs = targetTypeAvailabilityInputs;
    }
    
    public TacticalTarget createTargetType() throws PWCGException
    {
        while (preferredTargetTypes.size() == 0)
        {
            formTargetPriorities();
            consolidatePreferredTargets();
            
            if (preferredTargetTypes.size() == 0)
            {
                double maxDistance = targetTypeAvailabilityInputs.getMaxDistance();
                maxDistance += 15000.0;
                targetTypeAvailabilityInputs.setMaxDistance(maxDistance);
            }

            if (targetTypeAvailabilityInputs.getMaxDistance() > 400000.0)
            {
                throw new PWCGException("Failed to find a target type within a reasonable range");
            }
        }
        
        
        return getTargetType();            
    }

    public void formTargetPriorities() throws PWCGException 
    {
        checkTargetAvailability(TacticalTarget.TARGET_TROOP_CONCENTRATION, 1);
        checkTargetAvailability(TacticalTarget.TARGET_ASSAULT, 2);
        checkTargetAvailability(TacticalTarget.TARGET_DEFENSE, 2);
        checkTargetAvailability(TacticalTarget.TARGET_ARTILLERY, 1);
        checkTargetAvailability(TacticalTarget.TARGET_TRANSPORT, 3);
        checkTargetAvailability(TacticalTarget.TARGET_TRAIN, 2);
        checkTargetAvailability(TacticalTarget.TARGET_AIRFIELD, 1);
        checkTargetAvailability(TacticalTarget.TARGET_DRIFTER, 1);
        checkTargetAvailability(TacticalTarget.TARGET_SHIPPING, 1);
    }
    
    private void checkTargetAvailability(TacticalTarget targetType, int numInstancesForList) throws PWCGException
    {
        TargetTypeAvailability targetTypeAvailability = new TargetTypeAvailability(targetTypeAvailabilityInputs.getSide(), targetTypeAvailabilityInputs.getDate());
        double distanceOfClosestInstanceToReference = targetTypeAvailability.getTargetTypeAvailability(
                targetType, 
                targetTypeAvailabilityInputs.getTargetGeneralLocation(), 
                targetTypeAvailabilityInputs.getMaxDistance());
        
        if (distanceOfClosestInstanceToReference <= targetTypeAvailabilityInputs.getPreferredDistance())
        {
            addTargetTypeToList(targetType, preferredTargetTypes,  numInstancesForList);
        }
        else if (distanceOfClosestInstanceToReference <= targetTypeAvailabilityInputs.getMaxDistance())
        {
            addTargetTypeToList(targetType, longRangeTargetTypes,  numInstancesForList);
        }
    }

    private void addTargetTypeToList(TacticalTarget targetType, List <TacticalTarget> targetTypes, int numInstancesForList)
    {
        for (int i = 0; i < numInstancesForList; ++i)
        {
            targetTypes.add(targetType);
        }
    }

    private void consolidatePreferredTargets() throws PWCGException
    {
        if (preferredTargetTypes.size() < 3)
        {
            preferredTargetTypes.addAll(longRangeTargetTypes);
        }
    }

    private TacticalTarget getTargetType() throws PWCGException
    {        
        TacticalTarget targetType = getPreferredTargetType();
        if (targetType == TacticalTarget.TARGET_NONE)
        {
            targetType = getTargetTypeFromPrioritizedList();
        }
        return targetType;
    }

    private TacticalTarget getPreferredTargetType() throws PWCGException
    {        
        TargetPreferenceManager targetPreferenceManager = PWCGContextManager.getInstance().getCurrentMap().getTargetPreferenceManager();
        TacticalTarget targetType = targetPreferenceManager.getTargetPreferenceSet().getTargetPreferenceToUse(targetTypeAvailabilityInputs.getDate(), targetTypeAvailabilityInputs.getSide());
        if (isPreferredAvailable(targetType))
        {
            return targetType;
        }
        
        return TacticalTarget.TARGET_NONE;
    }
    
    private boolean isPreferredAvailable(TacticalTarget preferredTargetType)
    {
        for (TacticalTarget targetType : preferredTargetTypes)
        {
            if (targetType == preferredTargetType)
            {
                return true;
            }
        }
        
        return false;
    }

    private TacticalTarget getTargetTypeFromPrioritizedList() throws PWCGException
    {        
        TacticalTarget targetType = preferredTargetTypes.get(RandomNumberGenerator.getRandom(preferredTargetTypes.size()));
        return targetType;
    }

    public List<TacticalTarget> getPreferredTargetTypes()
    {
        return preferredTargetTypes;
    }

    public List<TacticalTarget> getLongRangeTargetTypes()
    {
        return longRangeTargetTypes;
    }
    
    
}
