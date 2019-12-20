package pwcg.mission.flight.escort;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.initialposition.FlightPositionAirStart;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightAdjuster
{
    private PlayerEscortFlight escortFlight = null;
    private Flight escortedFlight = null;

    public PlayerEscortFlightAdjuster(PlayerEscortFlight escortFlight, Flight escortedFlight)
    {
        this.escortFlight = escortFlight;
        this.escortedFlight = escortedFlight;
    }

    public void adjustEscortedFlightForEscort() throws PWCGException
    {
        moveIngressWaypoint();
        moveAirStartWaypoint();
        moveAirStartToRendezvous();
        moveEgressWaypoint();
        adjustEscortedAltitudeToMatchEscortAltitude();
    }
    
    private void moveIngressWaypoint() throws PWCGException
    {
        McuWaypoint ingressWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint targetApproachWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_TARGET_APPROACH);

        Coordinate adjustedPositionForIngressWP = rendezvousWP.getPosition();
        adjustedPositionForIngressWP.setYPos(rendezvousWP.getPosition().getYPos() - 500);
        ingressWP.setPosition(adjustedPositionForIngressWP);
        
        double angleBetweenRendezvousAndTargetApproach = MathUtils.calcAngle(rendezvousWP.getPosition(), targetApproachWP.getPosition());
        Orientation airStartOrientation = new Orientation(angleBetweenRendezvousAndTargetApproach);
        ingressWP.setOrientation(airStartOrientation);
    }

    private void moveAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWP = escortedFlight.getWaypointPackage().getWaypointByType(WaypointType.AIR_START_WAYPOINT);
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        
        double angleAirfieldToRendezvous = MathUtils.calcAngle(escortedFlight.getPosition(), rendezvousWP.getPosition());
        double angleAirfieldToRendezvousReversed = MathUtils.adjustAngle(angleAirfieldToRendezvous, 180);
        Coordinate adjustedPositionForAirStartWP = MathUtils.calcNextCoord(rendezvousWP.getPosition(), angleAirfieldToRendezvousReversed, 3000.0);
        airStartWP.setPosition(adjustedPositionForAirStartWP);
   
        Orientation airStartOrientation = new Orientation(angleAirfieldToRendezvous);
        airStartWP.setOrientation(airStartOrientation);
    }

    private void moveAirStartToRendezvous() throws PWCGException
    {
        FlightPositionAirStart.createPlanePositionAirStart(escortedFlight);
    }
    
    private void moveEgressWaypoint() throws PWCGException
    {
        McuWaypoint egressWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_EGRESS);
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint targetApproachWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint targetEgressWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_TARGET_EGRESS);

        double angleBetweenTargetApproachAndTargetEgress = MathUtils.calcAngle(targetApproachWP.getPosition(), targetEgressWP.getPosition());
        Coordinate adjustedPositionForEgressWP = MathUtils.calcNextCoord(rendezvousWP.getPosition(), angleBetweenTargetApproachAndTargetEgress, 5000.0);
        egressWP.setPosition(adjustedPositionForEgressWP);

        Orientation airStartOrientation = new Orientation(angleBetweenTargetApproachAndTargetEgress);
        egressWP.setOrientation(airStartOrientation);
    }

    private void adjustEscortedAltitudeToMatchEscortAltitude() throws PWCGException
    {
        double bombingAltitude = calculateAdjustedBombingAltitude();
        for (McuWaypoint waypoint : escortedFlight.getAllFlightWaypoints())
        {
            waypoint.getPosition().setYPos(bombingAltitude);
        }
    }

    private double calculateAdjustedBombingAltitude() throws PWCGException
    {
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int additionalAltitudeForEscort = productSpecificConfiguration.getAdditionalAltitudeForEscort();
        double bombingAltitude = rendezvousWP.getPosition().getYPos() - additionalAltitudeForEscort;
        return bombingAltitude;
    }
}
