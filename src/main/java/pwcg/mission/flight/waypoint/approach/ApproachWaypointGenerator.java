package pwcg.mission.flight.waypoint.approach;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class ApproachWaypointGenerator
{
    public static McuWaypoint createApproachWaypoint(Flight flight) throws PWCGException  
    {
        PWCGLocation landingLocation = flight.getAirfield().getLandingLocation();
        int initialWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
        Coordinate approachCoords = MathUtils.calcNextCoord(landingLocation.getPosition(), landingLocation.getOrientation().getyOri() - 180, initialWaypointDistance);
        
        int ApproachWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.ApproachWaypointAltitudeKey);
        approachCoords.setYPos(flight.getAirfield().getPosition().getYPos() + ApproachWaypointAltitude);

        Orientation orient = new Orientation();
        orient.setyOri(landingLocation.getOrientation().getyOri());

        McuWaypoint approachWP = WaypointFactory.createLandingApproachWaypointType();
        approachWP.setTriggerArea(McuWaypoint.LAND_AREA);
        approachWP.setSpeed(220);
        approachWP.setPosition(approachCoords);
        approachWP.setOrientation(orient);
        
        return approachWP;
    }

}
