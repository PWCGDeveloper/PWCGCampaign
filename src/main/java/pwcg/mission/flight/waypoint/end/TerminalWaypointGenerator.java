package pwcg.mission.flight.waypoint.end;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class TerminalWaypointGenerator
{
    public static McuWaypoint createTerminalWaypoint(IFlight flight) throws PWCGException  
    {        
        int LandingApproachWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingApproachWaypointDistanceKey);
        Coordinate approachCoords = MathUtils.calcNextCoord(flight.getFlightInformation().getHomePosition(), 0, LandingApproachWaypointDistance);
        
        approachCoords.setYPos(1000);

        Orientation orient = new Orientation();
        orient.setyOri(0);

        McuWaypoint approachWP = WaypointFactory.createLandingApproachWaypointType();
        approachWP.setTriggerArea(McuWaypoint.LAND_AREA);
        approachWP.setSpeed(flight.getFlightCruisingSpeed());
        approachWP.setPosition(approachCoords);
        approachWP.setOrientation(orient);
        approachWP.setPriority(WaypointPriority.PRIORITY_MED);
        
        return approachWP;
    }

}
