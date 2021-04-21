package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
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
        SkirmishTargetDefinitionBuilder skirmishTargetDefinitionBuilder = new SkirmishTargetDefinitionBuilder(flightInformation);
        TargetDefinition targetDefinition = skirmishTargetDefinitionBuilder.findIconicTarget();
        if (targetDefinition == null)
        {
            targetDefinition = buildCommonTargetDefinition();
        }
        return targetDefinition;
    }
    
    public TargetDefinition buildCommonTargetDefinition() throws PWCGException
    {
        TargetDefinitionCollector targetDefinitionCollector = new TargetDefinitionCollector(flightInformation);
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
        if (TestDriver.getInstance().isEnabled())
        {
            if (TestDriver.getInstance().getTestPlayerTacticalTargetType() != TargetType.TARGET_NONE)
            {
                List<TargetType> testTargetTypes = new ArrayList<>();
                testTargetTypes.add(TestDriver.getInstance().getTestPlayerTacticalTargetType());
                return testTargetTypes;
            }
        }
        
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorTactical.getTargetTypePriorities(flightInformation);
        TargetType skirmishTargetType = getSkirmishPreferredTargetType();
        if (skirmishTargetType != TargetType.TARGET_NONE)
        {
            shuffledTargetTypes.add(0, skirmishTargetType);
        }
        
        return shuffledTargetTypes;
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
}
