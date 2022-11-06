package pwcg.mission.target;

import java.util.List;

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
        GroundTargetDefinitionCollector targetDefinitionCollector = new GroundTargetDefinitionCollector(flightInformation);
        List<TargetDefinition> allTargets = targetDefinitionCollector.collectTargetDefinition();
        TargetDefinition targetDefinition =  findTarget(allTargets);
        return targetDefinition;
    }

    private TargetDefinition findTarget(List<TargetDefinition> availableTargets) throws PWCGException
    {
        TargetDefinitionPreferenceBuilder targetDefinitionPreferenceBuilder = new TargetDefinitionPreferenceBuilder(flightInformation);
        List<TargetType> shuffledTargetTypes = targetDefinitionPreferenceBuilder.getTargetPreferences();
        for (TargetType desiredTargetType : shuffledTargetTypes)
        {
            TargetDefinition targetDefinition = getDesiredTargetType(availableTargets, desiredTargetType);
            if (targetDefinition != null)
            {
                return targetDefinition;
            }
        }
        throw new PWCGException ("No targets available in mission");
    }

    private TargetDefinition getDesiredTargetType(List<TargetDefinition> availableTargets, TargetType desiredTargetType)
    {
        for (TargetDefinition targetDefinition : availableTargets)
        {
            if (desiredTargetType == targetDefinition.getTargetType())
            {
                return targetDefinition;
            }
        }
        return null;
    }
}
