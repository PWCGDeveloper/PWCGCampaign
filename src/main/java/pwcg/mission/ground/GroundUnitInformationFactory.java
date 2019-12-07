package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class GroundUnitInformationFactory
{
    public static GroundUnitInformation buildGroundUnitInformation(
            Campaign campaign, 
            ICountry country, 
            String name,
            TacticalTarget targetType,
            Coordinate startCoords, 
            Coordinate targetCoords,
            Orientation orientation,
            boolean isPlayerTarget) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setCountry(country);
        groundUnitInformation.setName(name);
        groundUnitInformation.setDate(campaign.getDate());
        groundUnitInformation.setTargetType(targetType);
        groundUnitInformation.setPosition(startCoords);
        groundUnitInformation.setDestination(targetCoords);
        
        GroundUnitSize unitSize = GroundUnitSizeBuilder.calcNumUnitsByConfig(campaign, isPlayerTarget);
        groundUnitInformation.setUnitSize(unitSize);
        
        orientation = determineOrientation(startCoords, targetCoords, orientation);
        groundUnitInformation.setOrientation(orientation);

        return groundUnitInformation;
    }

    public static GroundUnitInformation buildGroundUnitInformation(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setCountry(targetDefinition.getTargetCountry());
        groundUnitInformation.setName(targetDefinition.getTargetName());
        groundUnitInformation.setDate(campaign.getDate());
        groundUnitInformation.setTargetType(targetDefinition.getTargetType());
        groundUnitInformation.setPosition(targetDefinition.getTargetPosition());
        groundUnitInformation.setDestination(targetDefinition.getTargetPosition());
        
        GroundUnitSize unitSize = GroundUnitSizeBuilder.calcNumUnitsByConfig(campaign, targetDefinition.isPlayerTarget());
        groundUnitInformation.setUnitSize(unitSize);
        
        Orientation orientation = determineOrientation(targetDefinition.getTargetPosition(), targetDefinition.getTargetPosition(), targetDefinition.getTargetOrientation());
        groundUnitInformation.setOrientation(orientation);

        return groundUnitInformation;
    }

    private static Orientation determineOrientation(Coordinate position, Coordinate destination, Orientation orientation) throws PWCGException
    {
        if (orientation == null)
        {
            orientation = Orientation.createRandomOrientation();
        }
        else
        {
            if (position.equals(destination))
            {
                return orientation;
            }
            else
            {
                orientation = createOrientation(position, destination);
            }
        }
        
        return orientation;
    }

    private static Orientation createOrientation(Coordinate position, Coordinate destination) throws PWCGException
    {
        Orientation orientation = null;
        if (!position.equals(destination))
        {
            double facing = MathUtils.calcAngle(position, destination);
            orientation = new Orientation(facing);
        }
        else
        {
            orientation = Orientation.createRandomOrientation();
        }

        return orientation;
    }
}
