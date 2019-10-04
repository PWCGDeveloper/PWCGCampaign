package pwcg.mission.flight.escort;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPositionSetter;
import pwcg.mission.flight.waypoint.WaypointAction;
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
        moveAirStartToRendezvous();
        moveAirStartWaypoint();
        moveIngressWaypoint();
        moveEgressWaypoint();
        adjustEscortedAltitudeToMatchEscortAltitude();
    }
    
    private void moveAirStartToRendezvous() throws PWCGException
    {
        FlightPositionSetter.setEscortedFlightToRendezvous(escortFlight, escortedFlight);
    }

    private void moveAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_START);
        McuWaypoint targetApproachWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        
        double partialDistanceBetweenRendezvousAndTargetApproach = (MathUtils.calcDist(rendezvousWP.getPosition(), targetApproachWP.getPosition()) / 3.0);
        double angleBetweenRendezvousAndTargetApproach = MathUtils.calcAngle(rendezvousWP.getPosition(), targetApproachWP.getPosition());
        Coordinate adjustedPositionForAirStartWP = MathUtils.calcNextCoord(rendezvousWP.getPosition(), angleBetweenRendezvousAndTargetApproach, partialDistanceBetweenRendezvousAndTargetApproach);
        airStartWP.setPosition(adjustedPositionForAirStartWP);
        
        Orientation airStartOrientation = new Orientation(angleBetweenRendezvousAndTargetApproach);
        airStartWP.setOrientation(airStartOrientation);
    }
    
    private void moveIngressWaypoint() throws PWCGException
    {
        McuWaypoint ingressWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint targetApproachWP = escortedFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_TARGET_APPROACH);

        double partialDistanceBetweenRendezvousAndTargetApproach = (MathUtils.calcDist(rendezvousWP.getPosition(), targetApproachWP.getPosition()) / 2.0);
        double angleBetweenRendezvousAndTargetApproach = MathUtils.calcAngle(rendezvousWP.getPosition(), targetApproachWP.getPosition());
        Coordinate adjustedPositionForIngressWP = MathUtils.calcNextCoord(rendezvousWP.getPosition(), angleBetweenRendezvousAndTargetApproach, partialDistanceBetweenRendezvousAndTargetApproach);
        ingressWP.setPosition(adjustedPositionForIngressWP);
        
        Orientation airStartOrientation = new Orientation(angleBetweenRendezvousAndTargetApproach);
        ingressWP.setOrientation(airStartOrientation);
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
