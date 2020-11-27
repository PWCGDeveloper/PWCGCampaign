package pwcg.mission.flight.waypoint.end;

import pwcg.campaign.api.IAirfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class ApproachWaypointGenerator
{
    public static McuWaypoint createApproachWaypoint(IFlight flight, IAirfield landingAirfield) throws PWCGException  
    {        
        PWCGLocation landingLocation = landingAirfield.getLandingLocation(flight.getMission());
        int LandingApproachWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingApproachWaypointDistanceKey);
        Coordinate approachCoords = MathUtils.calcNextCoord(landingLocation.getPosition(), landingLocation.getOrientation().getyOri() - 180, LandingApproachWaypointDistance);
        
        int ApproachWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.ApproachWaypointAltitudeKey);
        approachCoords.setYPos(landingAirfield.getPosition().getYPos() + ApproachWaypointAltitude);

        Orientation orient = new Orientation();
        orient.setyOri(landingLocation.getOrientation().getyOri());

        McuWaypoint approachWP = WaypointFactory.createLandingApproachWaypointType();
        approachWP.setTriggerArea(McuWaypoint.LAND_AREA);
        approachWP.setSpeed(flight.getFlightCruisingSpeed());
        approachWP.setPosition(approachCoords);
        approachWP.setOrientation(orient);
        approachWP.setPriority(WaypointPriority.PRIORITY_MED);
        
        return approachWP;
    }

}
