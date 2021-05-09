package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetDefinitionCollector
{
    private FlightInformation flightInformation;

    public TargetDefinitionCollector(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<TargetDefinition> collectTargetDefinition() throws PWCGException
    {
        List<TargetDefinition> targetaDefinitionsForGroundUnit = createTargetDefinitionsForInfantry();
        List<TargetDefinition> targetaDefinitionsForStructure = createTargetDefinitionsForAssignedStructures();
        
        List<TargetDefinition> allTargets = new ArrayList<>();
        allTargets.addAll(targetaDefinitionsForGroundUnit);
        allTargets.addAll(targetaDefinitionsForStructure);
        return allTargets;
    }

    private List<TargetDefinition> createTargetDefinitionsForInfantry() throws PWCGException
    {
        TargetDefinitionBuilderInfantry targetSelector = new TargetDefinitionBuilderInfantry(flightInformation);
        List<TargetDefinition> targetaDefinitionsForGroundUnit = targetSelector.findInfantryGroundUnits();
        Collections.shuffle(targetaDefinitionsForGroundUnit);
        return targetaDefinitionsForGroundUnit;
    }

    private List<TargetDefinition> createTargetDefinitionsForAssignedStructures() throws PWCGException
    {
        TargetDefinitionBuilderStructural targetSelector = new TargetDefinitionBuilderStructural(flightInformation);
        List<TargetDefinition> targetaDefinitionsForStructures = targetSelector.findTargetStructures();
        Collections.shuffle(targetaDefinitionsForStructures);
        return targetaDefinitionsForStructures;
    }
}
