package pwcg.mission.target;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
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
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int radius = productSpecific.getLargeMissionRadius();

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(
                flightInformation,
                radius,
                proposedTargetPosition);
        
        List<TargetDefinition> availableTargets = strategicTargetLocator.getStrategicTargetAvailability();
        return availableTargets;
    }
    

    private TargetDefinition findStrategicTarget(List<TargetDefinition> availableTargets) throws PWCGException
    {
        Collections.shuffle(availableTargets);
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorStrategic.getTargetTypePriorities(flightInformation);
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
