package pwcg.mission.target;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.locator.StrategicTargetLocator;

public class TargetDefinitionBuilderStrategic implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public TargetDefinitionBuilderStrategic (FlightInformation flightInformation)
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
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorStrategic.getTargetTypePriorities(flightInformation);
        List<GroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getGroundUnitBuilder().getAllMissionGroundUnits();
        Collections.shuffle(shuffledGroundUnits);

        FrontLinesForMap frontlines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(flightInformation.getCampaign().getDate());
        for (TargetType desiredTargetType : shuffledTargetTypes)
        {
            for (TargetDefinition targetDefinition : availableTargets)
            {
                if (desiredTargetType == targetDefinition.getTargetType())
                {
                    if (frontlines.isFarFromFront(targetDefinition.getPosition(), flightInformation.getSquadron().determineSide(), flightInformation.getCampaign().getDate()))
                    {
                        return targetDefinition;
                    }
                }
            }
        }
        throw new PWCGException ("No strategic targets available in mission");
    }
}
