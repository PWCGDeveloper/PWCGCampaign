package pwcg.mission.ground;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;

public class GroundUnitInformationFactory
{
    public static GroundUnitInformation buildGroundUnitInformation(MissionBeginUnit missionBeginUnit, ICountry country, String name, TacticalTarget targetType, Coordinate position, Coordinate destination, Orientation orientation)
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setMissionBeginUnit(missionBeginUnit);
        groundUnitInformation.setCountry(country);
        groundUnitInformation.setName(name);
        groundUnitInformation.setTargetType(targetType);
        groundUnitInformation.setPosition(position);
        groundUnitInformation.setDestination(destination);
        groundUnitInformation.setOrientation(orientation);
        return groundUnitInformation;
    }
    
    public static GroundUnitInformation buildGroundUnitInformation(MissionBeginUnit missionBeginUnit, ICountry country, String name, TacticalTarget targetType, Coordinate position, Coordinate destination) throws PWCGException
    {
        Orientation orientation = createOrientation(position, destination);
                
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setMissionBeginUnit(missionBeginUnit);
        groundUnitInformation.setCountry(country);
        groundUnitInformation.setName(name);
        groundUnitInformation.setTargetType(targetType);
        groundUnitInformation.setPosition(position);
        groundUnitInformation.setDestination(destination);
        groundUnitInformation.setOrientation(orientation);
        return groundUnitInformation;
    }
    
    private static Orientation createOrientation(Coordinate position, Coordinate destination) throws PWCGException
    {
        Orientation orientation = new Orientation(RandomNumberGenerator.getRandom(360));
        if (!position.equals(destination))
        {
            double facing = MathUtils.calcAngle(position, destination);
            orientation = new Orientation(facing);
        }

        return orientation;
    }
}
