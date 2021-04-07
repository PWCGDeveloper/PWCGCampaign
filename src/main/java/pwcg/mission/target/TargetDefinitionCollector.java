package pwcg.mission.target;

import java.util.ArrayList;
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
        List<TargetDefinition> targetaDefinitionsForStructure = createTargetDefinitionsForStructures();
        
        List<TargetDefinition> allTargets = new ArrayList<>();
        allTargets.addAll(targetaDefinitionsForGroundUnit);
        allTargets.addAll(targetaDefinitionsForStructure);
        return allTargets;
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
}
