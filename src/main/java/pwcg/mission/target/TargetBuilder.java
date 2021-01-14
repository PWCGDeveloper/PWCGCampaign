package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;

public class TargetBuilder implements ITargetDefinitionBuilder
{
    private IFlightInformation flightInformation;

    public TargetBuilder(IFlightInformation flightInformation) throws PWCGException
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
        return findTarget(allTargets);

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
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorTactical.getTargetTypePriorities(flightInformation.getCampaign(), flightInformation.getSquadron(), flightInformation.getFlightType());

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
        throw new PWCGException ("No strategic targets available in mission");
    }
}
