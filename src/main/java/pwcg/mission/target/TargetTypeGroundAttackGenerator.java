package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.preference.TargetPreference;
import pwcg.campaign.target.preference.TargetPreferenceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.WeightedOddsCalculator;

public class TargetTypeGroundAttackGenerator
{
    private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    private Map<TargetType, Integer> preferredTargetTypes = new TreeMap<>();
    private Map<TargetType, Integer> longRangeTargetTypes = new TreeMap<>();

    private Campaign campaign;
    private Squadron squadron;

    public TargetTypeGroundAttackGenerator(Campaign campaign, Squadron squadron, TargetTypeAvailabilityInputs targetTypeAvailabilityInputs)
    {
        this.campaign = campaign;
        this.squadron = squadron;
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
        
        addMapTargetPreferences();
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
        TargetPriorityGenerator targetPriorityGenerator = new TargetPriorityGenerator(campaign);
        Map<TargetType, Integer> targetTypePriorities = targetPriorityGenerator.getTargetTypePriorities(squadron);
        for (TargetType targetType : targetTypePriorities.keySet())
        {
            checkTargetAvailabilityByRange(targetType, targetTypePriorities.get(targetType));
        }
    }

    private void formTargetPrioritiesForAiFlight() throws PWCGException 
    {
        checkTargetAvailabilityByRange(TargetType.TARGET_ARTILLERY, 1);
        checkTargetAvailabilityByRange(TargetType.TARGET_TRANSPORT, 3);
        checkTargetAvailabilityByRange(TargetType.TARGET_TRAIN, 2);
        checkTargetAvailabilityByRange(TargetType.TARGET_AIRFIELD, 1);
        checkTargetAvailabilityByRange(TargetType.TARGET_DRIFTER, 1);
        checkTargetAvailabilityByRange(TargetType.TARGET_SHIPPING, 1);
    }
    
    private void checkTargetAvailabilityByRange(TargetType targetType, int weight) throws PWCGException
    {
        TargetTypeAvailability targetTypeAvailability = new TargetTypeAvailability(targetTypeAvailabilityInputs.getSide(), targetTypeAvailabilityInputs.getDate());
        double distanceOfClosestInstanceToReference = targetTypeAvailability.getTargetTypeAvailability(
                targetType, 
                targetTypeAvailabilityInputs.getTargetGeneralLocation(), 
                targetTypeAvailabilityInputs.getMaxDistance());
        
        if (distanceOfClosestInstanceToReference <= targetTypeAvailabilityInputs.getPreferredDistance())
        {
            preferredTargetTypes.put(targetType, weight);
        }
        else if (distanceOfClosestInstanceToReference <= targetTypeAvailabilityInputs.getMaxDistance())
        {
            longRangeTargetTypes.put(targetType, weight);
        }
    }
    
    private void addMapTargetPreferences () throws PWCGException
    {
        TargetPreferenceManager targetPreferenceManager = PWCGContext.getInstance().getCurrentMap().getTargetPreferenceManager();
        TargetPreference targetPreference = targetPreferenceManager.getTargetPreferenceSet().getTargetPreferenceToUse(targetTypeAvailabilityInputs.getDate(), targetTypeAvailabilityInputs.getSide());
        if (targetPreference != null)
        {
            if (preferredTargetTypes.containsKey(targetPreference.getTargetType()))
            {
                int weight = preferredTargetTypes.get(targetPreference.getTargetType());
                weight += targetPreference.getOddsOfUse();
                preferredTargetTypes.put(targetPreference.getTargetType(), weight);
            }
        }
    }

    private void consolidatePreferredTargets() throws PWCGException
    {
        if (preferredTargetTypes.size() < 3)
        {
            preferredTargetTypes.putAll(longRangeTargetTypes);
        }
    }

    private TargetType getTargetType() throws PWCGException
    {        
        TargetType selectedTargetType = TargetType.TARGET_NONE;
        if (!preferredTargetTypes.isEmpty())
        {
            List<Integer> weightedOdds = new ArrayList<>();
            List<TargetType> targetTypes = new ArrayList<>();

            for (TargetType targetType : preferredTargetTypes.keySet())
            {
                int targetWeight = preferredTargetTypes.get(targetType);
                weightedOdds.add(targetWeight);
                targetTypes.add(targetType);
            }
            
            int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
            selectedTargetType = targetTypes.get(selectedIndex);
        }
            
        return selectedTargetType;
    }

    public Map<TargetType, Integer> getPreferredTargetTypes()
    {
        return preferredTargetTypes;
    }

    public Map<TargetType, Integer> getLongRangeTargetTypes()
    {
        return longRangeTargetTypes;
    }
}
