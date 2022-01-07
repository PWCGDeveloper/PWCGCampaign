package pwcg.mission.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class GroundTargetDefinitionCollector
{
    private FlightInformation flightInformation;

    public GroundTargetDefinitionCollector(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<TargetDefinition> collectTargetDefinition() throws PWCGException
    {
        List<TargetDefinition> targetaDefinitionsForGroundUnit = createTargetDefinitionsForInfantry();
        
        List<TargetDefinition> allTargets = new ArrayList<>();
        allTargets.addAll(targetaDefinitionsForGroundUnit);
        return allTargets;
    }

    private List<TargetDefinition> createTargetDefinitionsForInfantry() throws PWCGException
    {
        TargetDefinitionBuilderInfantry targetSelector = new TargetDefinitionBuilderInfantry(flightInformation);
        List<TargetDefinition> targetaDefinitionsForGroundUnit = targetSelector.findInfantryGroundUnits();
        Collections.shuffle(targetaDefinitionsForGroundUnit);
        return targetaDefinitionsForGroundUnit;
    }
}
