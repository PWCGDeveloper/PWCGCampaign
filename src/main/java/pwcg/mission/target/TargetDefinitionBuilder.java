package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadron.TargetPreferencePeriod;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class TargetDefinitionBuilder implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionBuilder(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }
    
    public TargetDefinition buildTargetDefinition() throws PWCGException
    {
        GroundTargetDefinitionCollector targetDefinitionCollector = new GroundTargetDefinitionCollector(flightInformation);
        List<TargetDefinition> allTargets = targetDefinitionCollector.collectTargetDefinition();
        TargetDefinition targetDefinition =  findTarget(allTargets);
        return targetDefinition;
    }

    private TargetDefinition findTarget(List<TargetDefinition> availableTargets) throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = getTargetPreferences();
        for (TargetType desiredTargetType : shuffledTargetTypes)
        {
            for (TargetDefinition targetDefinition : availableTargets)
            {
                if (desiredTargetType == targetDefinition.getTargetType())
                {
                    return targetDefinition;
                }
            }
        }
        throw new PWCGException ("No targets available in mission");
    }

    private List<TargetType> getTargetPreferences() throws PWCGException
    {
        List<TargetType> targetTypes = new ArrayList<>();

        TargetType testTargetType = getTestTargetType();
        if (testTargetType != TargetType.TARGET_NONE)
        {
            targetTypes.add(testTargetType);
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
  
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorTactical.getTargetTypePriorities(flightInformation);
        targetTypes.addAll(shuffledTargetTypes);
        
        return targetTypes;
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
