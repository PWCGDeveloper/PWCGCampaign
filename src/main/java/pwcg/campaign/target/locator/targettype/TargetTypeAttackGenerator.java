package pwcg.campaign.target.locator.targettype;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.target.TargetType;

public class TargetTypeAttackGenerator
{
    private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    private List <TargetType> preferredTargetTypes = new ArrayList<TargetType>();
    private List <TargetType> longRangeTargetTypes = new ArrayList<TargetType>();

    public TargetTypeAttackGenerator(TargetTypeAvailabilityInputs targetTypeAvailabilityInputs)
    {
        this.targetTypeAvailabilityInputs = targetTypeAvailabilityInputs;
    }
    
    public TargetType createTargetType() throws PWCGException
    {
        while (preferredTargetTypes.size() == 0)
        {
            formTargetPriorities();
            consolidatePreferredTargets();
            expandTargetZone();
        }
        
        return getTargetType();            
    }

    private void expandTargetZone() throws PWCGException
    {
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

    public void formTargetPriorities() throws PWCGException
    {
        if (targetTypeAvailabilityInputs.isUseMinimalTargetSet())
        {
            formTargetPrioritiesForAiFlight();
        }
        else
        {
            formTargetPrioritiesForPlayerFlight();
        }
    }
    
    private void formTargetPrioritiesForPlayerFlight() throws PWCGException 
    {
        checkTargetAvailability(TargetType.TARGET_ASSAULT, 2);
        checkTargetAvailability(TargetType.TARGET_DEFENSE, 2);
        checkTargetAvailability(TargetType.TARGET_ARTILLERY, 1);
        checkTargetAvailability(TargetType.TARGET_TRANSPORT, 3);
        checkTargetAvailability(TargetType.TARGET_TRAIN, 2);
        checkTargetAvailability(TargetType.TARGET_AIRFIELD, 1);
        checkTargetAvailability(TargetType.TARGET_DRIFTER, 1);
        checkTargetAvailability(TargetType.TARGET_SHIPPING, 1);
    }

    private void formTargetPrioritiesForAiFlight() throws PWCGException 
    {
        checkTargetAvailability(TargetType.TARGET_ARTILLERY, 1);
        checkTargetAvailability(TargetType.TARGET_TRANSPORT, 3);
        checkTargetAvailability(TargetType.TARGET_TRAIN, 2);
        checkTargetAvailability(TargetType.TARGET_AIRFIELD, 1);
        checkTargetAvailability(TargetType.TARGET_DRIFTER, 1);
        checkTargetAvailability(TargetType.TARGET_SHIPPING, 1);
    }
    
    private void checkTargetAvailability(TargetType targetType, int numInstancesForList) throws PWCGException
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

    private void addTargetTypeToList(TargetType targetType, List <TargetType> targetTypes, int numInstancesForList)
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

    private TargetType getTargetType() throws PWCGException
    {        
        TargetType targetType = getPreferredTargetType();
        if (targetType == TargetType.TARGET_NONE)
        {
            targetType = getTargetTypeFromPrioritizedList();
        }
        return targetType;
    }

    private TargetType getPreferredTargetType() throws PWCGException
    {        
        TargetPreferenceManager targetPreferenceManager = PWCGContext.getInstance().getCurrentMap().getTargetPreferenceManager();
        TargetType targetType = targetPreferenceManager.getTargetPreferenceSet().getTargetPreferenceToUse(targetTypeAvailabilityInputs.getDate(), targetTypeAvailabilityInputs.getSide());
        if (isPreferredAvailable(targetType))
        {
            return targetType;
        }
        
        return TargetType.TARGET_NONE;
    }
    
    private boolean isPreferredAvailable(TargetType preferredTargetType)
    {
        for (TargetType targetType : preferredTargetTypes)
        {
            if (targetType == preferredTargetType)
            {
                return true;
            }
        }
        
        return false;
    }

    private TargetType getTargetTypeFromPrioritizedList() throws PWCGException
    {        
        TargetType targetType = preferredTargetTypes.get(RandomNumberGenerator.getRandom(preferredTargetTypes.size()));
        return targetType;
    }

    public List<TargetType> getPreferredTargetTypes()
    {
        return preferredTargetTypes;
    }

    public List<TargetType> getLongRangeTargetTypes()
    {
        return longRangeTargetTypes;
    }
}
