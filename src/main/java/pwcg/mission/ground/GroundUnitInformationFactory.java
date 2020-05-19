package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.target.TargetType;

public class GroundUnitInformationFactory
{
    public static GroundUnitInformation buildGroundUnitInformation(
            Campaign campaign, 
            ICountry country, 
            TargetType targetType,
            Coordinate startCoords, 
            Coordinate targetCoords,
            Orientation orientation) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setCountry(country);
        groundUnitInformation.setName(targetType.getTargetName());
        groundUnitInformation.setDate(campaign.getDate());
        groundUnitInformation.setTargetType(targetType);
        groundUnitInformation.setPosition(startCoords);
        groundUnitInformation.setDestination(targetCoords);
        
        GroundUnitSize unitSize = GroundUnitSize.calcNumUnitsByConfig(campaign);
        groundUnitInformation.setUnitSize(unitSize);
        
        orientation = determineOrientation(startCoords, targetCoords, orientation);
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
