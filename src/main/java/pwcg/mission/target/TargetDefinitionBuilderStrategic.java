package pwcg.mission.target;

import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.locator.StrategicTargetLocator;

public class TargetDefinitionBuilderStrategic implements ITargetDefinitionBuilder
{
    private IFlightInformation flightInformation;

    public TargetDefinitionBuilderStrategic (IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public TargetDefinition buildTargetDefinition () throws PWCGException
    {
        Coordinate proposedTargetPosition = flightInformation.getTargetSearchStartLocation();
        List<TargetDefinition> availableTargets = getAvailableStrategicTargets(proposedTargetPosition);

        TargetDefinition targetDefinition = findStrategicTarget(availableTargets);

        return targetDefinition;
    }

    private List<TargetDefinition> getAvailableStrategicTargets(Coordinate proposedTargetPosition) throws PWCGException
    {
        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(
                flightInformation,
                Double.valueOf(targetRadius.getInitialTargetRadius()).intValue(),
                proposedTargetPosition);
        
        List<TargetDefinition> availableTargets = strategicTargetLocator.getStrategicTargetAvailability();
        return availableTargets;
    }
    

    private TargetDefinition findStrategicTarget(List<TargetDefinition> availableTargets) throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorStrategic.getTargetTypePriorities(flightInformation.getCampaign(), flightInformation.getSquadron());
        List<GroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits();
        Collections.shuffle(shuffledGroundUnits);

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
