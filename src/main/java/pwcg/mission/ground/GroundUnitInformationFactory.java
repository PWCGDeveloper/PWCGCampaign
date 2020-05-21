package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
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
        groundUnitInformation.setOrientation(orientation);
        
        GroundUnitSize unitSize = GroundUnitSize.calcNumUnitsByConfig(campaign);
        groundUnitInformation.setUnitSize(unitSize);

        return groundUnitInformation;
    }
}
