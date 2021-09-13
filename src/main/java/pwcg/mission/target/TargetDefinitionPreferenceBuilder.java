package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadron.TargetPreferencePeriod;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class TargetDefinitionPreferenceBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionPreferenceBuilder(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }
    
    public List<TargetType> getTargetPreferences() throws PWCGException
    {
        List<TargetType> targetTypes = new ArrayList<>();

        addSpecializedTargetTypeOverrides(targetTypes);
  
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorTactical.getTargetTypePriorities(flightInformation);
        targetTypes.addAll(shuffledTargetTypes);
        
        return targetTypes;
    }

    private void addSpecializedTargetTypeOverrides(List<TargetType> targetTypes) throws PWCGException
    {
        TargetType testTargetType = getTestTargetType();
        if (testTargetType != TargetType.TARGET_NONE)
        {
            targetTypes.add(testTargetType);
        }
        
        TargetType roleBasedTargetType = flightInformation.getRoleBasedTarget();
        if (roleBasedTargetType != TargetType.TARGET_NONE)
        {
            targetTypes.add(flightInformation.getRoleBasedTarget());
        }

        TargetType skirmishTargetType = getSkirmishPreferredTargetType();
        if (skirmishTargetType != TargetType.TARGET_NONE)
        {
            targetTypes.add(skirmishTargetType);
        }

        TargetType squadronPreferenceTargetType = getSquadronPreferredTargetType();
        if (squadronPreferenceTargetType != TargetType.TARGET_NONE)
        {
            targetTypes.add(squadronPreferenceTargetType);
        }

        if (flightInformation.getFlightType() == FlightTypes.GROUND_HUNT)
        {
            targetTypes.addAll(addTransportTargets());
        }
    }

    private List<TargetType> addTransportTargets()
    {
        List<TargetType> huntTargetTypes = new ArrayList<>();
        huntTargetTypes.add(TargetType.TARGET_TRAIN);
        huntTargetTypes.add(TargetType.TARGET_TRAIN);
        huntTargetTypes.add(TargetType.TARGET_TRANSPORT);
        huntTargetTypes.add(TargetType.TARGET_TRANSPORT);
        huntTargetTypes.add(TargetType.TARGET_TRANSPORT);
        huntTargetTypes.add(TargetType.TARGET_TRANSPORT);
        huntTargetTypes.add(TargetType.TARGET_SHIPPING);
        Collections.shuffle(huntTargetTypes);
        return huntTargetTypes;
    }

    private TargetType getTestTargetType()
    {
        if (TestDriver.getInstance().isEnabled())
        {
            if (TestDriver.getInstance().getTestPlayerTacticalTargetType() != TargetType.TARGET_NONE)
            {
                List<TargetType> testTargetTypes = new ArrayList<>();
                testTargetTypes.add(TestDriver.getInstance().getTestPlayerTacticalTargetType());
                if (testTargetTypes.size() > 0)
                {
                    return testTargetTypes.get(0);
                }
            }
        }
        
        return TargetType.TARGET_NONE;
    }
    
    private TargetType getSkirmishPreferredTargetType() throws PWCGException
    {
        TargetType skirmishTargetType = TargetType.TARGET_NONE;
        
        Skirmish skirmish = flightInformation.getMission().getSkirmish();
        if (skirmish != null)
        {
            skirmishTargetType = skirmish.getTargetTypeForFlightType(flightInformation.getFlightType(), flightInformation.getSquadron().determineSide());
        }
        return skirmishTargetType;
    }
    
    private TargetType getSquadronPreferredTargetType() throws PWCGException
    {
        TargetType squadronPreferredTargetType = TargetType.TARGET_NONE;
        
        TargetPreferencePeriod targetPreferencePeriod = flightInformation.getSquadron().getTargetPreference(flightInformation.getCampaign().getDate());
        if (targetPreferencePeriod != null)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < targetPreferencePeriod.getTargetPreferenceOdds())
            {
                squadronPreferredTargetType = targetPreferencePeriod.getTargetType();
            }
        }
        return squadronPreferredTargetType;
    }
}
