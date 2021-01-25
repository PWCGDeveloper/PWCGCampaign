package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetBuilder implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetBuilder(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {
        List<TargetDefinition> targetaDefinitionsForGroundUnit = createTargetDefinitionsForInfantry();
        List<TargetDefinition> targetaDefinitionsForStructure = createTargetDefinitionsForStructures();
        
        List<TargetDefinition> allTargets = new ArrayList<>();
        allTargets.addAll(targetaDefinitionsForGroundUnit);
        allTargets.addAll(targetaDefinitionsForStructure);
        TargetDefinition targetDefinition =  findTarget(allTargets);
        
        flightInformation.setTargetDefinition(targetDefinition);
        return targetDefinition;
    }

    private List<TargetDefinition> createTargetDefinitionsForInfantry() throws PWCGException
    {
        TargetDefinitionBuilderInfantry targetSelector = new TargetDefinitionBuilderInfantry(flightInformation);
        List<TargetDefinition> targetaDefinitionsForGroundUnit = targetSelector.findInfantryGroundUnits();
        return targetaDefinitionsForGroundUnit;
    }

    private List<TargetDefinition> createTargetDefinitionsForStructures() throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(flightInformation);
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findStructures();
        return targetaDefinitionsForStructures;
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
        return shuffledTargetTypes;
    }
}
