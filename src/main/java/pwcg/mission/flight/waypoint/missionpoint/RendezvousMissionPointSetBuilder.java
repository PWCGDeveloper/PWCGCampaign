package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class RendezvousMissionPointSetBuilder
{
    private IFlight flightThatNeedsEscort;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public RendezvousMissionPointSetBuilder(IFlight escortedFlight)
    {
        this.flightThatNeedsEscort = escortedFlight;
    }

    public IMissionPointSet createFlightRendezvousToPickUpEscort(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint rendezvousWaypoint = createRendezvousWaypoint(ingressWaypoint);

        missionPointSet.addWaypoint(rendezvousWaypoint);
        return missionPointSet;
    }
    
    private McuWaypoint createRendezvousWaypoint(McuWaypoint ingressWaypoint) throws PWCGException
    {
        Coordinate outpoundPosition = determineOutboundPosition();
        Coordinate ingressPosition = ingressWaypoint.getPosition();
        Coordinate rendezvousWaypointPosition = calculateRendezvousPosition(outpoundPosition, ingressPosition);
        McuWaypoint rendezvousWaypoint = buildRendezvousWaypoint(rendezvousWaypointPosition);
        return rendezvousWaypoint;
    }
    
    private Coordinate determineOutboundPosition() throws PWCGException
    {
        Coordinate outpoundPosition = null;
        for (McuWaypoint waypoint : flightThatNeedsEscort.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWaypointType() == WaypointType.CLIMB_WAYPOINT)
            {
                outpoundPosition = waypoint.getPosition();
            }
            else if (waypoint.getWaypointType() == WaypointType.TAKEOFF_WAYPOINT)
            {
                outpoundPosition = waypoint.getPosition();
            }
        }
        
        if (outpoundPosition == null)
        {
            outpoundPosition = flightThatNeedsEscort.getFlightHomePosition();
        }

        return outpoundPosition;
    }

    private Coordinate calculateRendezvousPosition(Coordinate outpoundPosition, Coordinate ingressPosition) throws PWCGException
    { 
        if (flightThatNeedsEscort.isPlayerFlight())
        {
            if (!flightThatNeedsEscort.getFlightInformation().isAirStart())
            {
                return calculatePlayerRendezvousPosition(outpoundPosition, ingressPosition);
            }
        }
    
        return calcualteAirStartRendezvousPosition(outpoundPosition, ingressPosition);
    }

    private Coordinate calculatePlayerRendezvousPosition(Coordinate outpoundPosition, Coordinate ingressPosition) throws PWCGException
    {
        double distanceOutboundToIngress = MathUtils.calcDist(outpoundPosition, ingressPosition);
        double angleFromOutboundToIngress = MathUtils.calcAngle(outpoundPosition, ingressPosition);
        Coordinate rendezvousPosition = MathUtils.calcNextCoord(outpoundPosition, angleFromOutboundToIngress, (distanceOutboundToIngress / 2));

        if (distanceOutboundToIngress > 12000)
        {
            rendezvousPosition = MathUtils.calcNextCoord(outpoundPosition, angleFromOutboundToIngress, 7000);
        }
        
        double rendezvousAltitude = calculatePlayerRendezvousAltitude(outpoundPosition, ingressPosition, rendezvousPosition);
        rendezvousPosition.setYPos(rendezvousAltitude);
        return rendezvousPosition;
    }

    private Coordinate calcualteAirStartRendezvousPosition(Coordinate outpoundPosition, Coordinate ingressPosition) throws PWCGException
    {
        double angleFromIngressToOutbound = MathUtils.calcAngle(outpoundPosition, ingressPosition);
        Coordinate rendezvousPosition = MathUtils.calcNextCoord(ingressPosition, angleFromIngressToOutbound, 5000);

        double rendezvousAltitude = ingressPosition.getYPos();
        rendezvousPosition.setYPos(rendezvousAltitude);
        return rendezvousPosition;
    }

    private double calculatePlayerRendezvousAltitude(Coordinate outpoundPosition, Coordinate ingressPosition, Coordinate rendezvousPosition) throws PWCGException
    {
        double altitudeDifference = Math.abs(ingressPosition.getYPos() - outpoundPosition.getYPos());
        double distanceOutboundToRendezvous = MathUtils.calcDist(outpoundPosition, rendezvousPosition);
        double distanceRendezvousToIngress = MathUtils.calcDist(rendezvousPosition, ingressPosition);
        double ratio = distanceOutboundToRendezvous / distanceRendezvousToIngress;
        double rendezvousAltitude = outpoundPosition.getYPos() + (altitudeDifference * ratio);
        return rendezvousAltitude;
    }

    private McuWaypoint buildRendezvousWaypoint(Coordinate rendezvousWaypointPosition)
    {
        McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
        rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
        rendezvousWaypoint.setPosition(rendezvousWaypointPosition);
        rendezvousWaypoint.setTargetWaypoint(false);
        rendezvousWaypoint.setSpeed(flightThatNeedsEscort.getFlightCruisingSpeed());
        return rendezvousWaypoint;
    }
}
