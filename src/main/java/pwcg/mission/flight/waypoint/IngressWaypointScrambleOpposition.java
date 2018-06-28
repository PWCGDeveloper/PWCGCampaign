package pwcg.mission.flight.waypoint;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointScrambleOpposition extends IngressWaypointBase
{
    public IngressWaypointScrambleOpposition(Flight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int waypointAltitude) throws PWCGException 
    {
        super(flight, lastPosition, targetPosition, waypointSpeed, waypointAltitude);
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        int attackIngressDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.GroundAttackIngressDistanceKey);
        double ingressAngle = MathUtils.calcAngle(lastPosition, flight.getTargetCoords());
        Coordinate groundIngressCoords = MathUtils.calcNextCoord(flight.getTargetCoords(), ingressAngle, attackIngressDistance);
        
        Coordinate coord = new Coordinate();
        coord.setXPos(groundIngressCoords.getXPos());
        coord.setZPos(groundIngressCoords.getZPos());
        coord.setYPos(waypointAltitude);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        ingressWP.setSpeed(waypointSpeed);
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(true);
        
        return ingressWP;
    }

}
