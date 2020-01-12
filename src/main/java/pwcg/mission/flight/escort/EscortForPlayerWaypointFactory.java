package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

public class EscortForPlayerWaypointFactory
{
    private IFlight escortFlight;
    private IFlight escortedFlight;
    private MissionPointEscortWaypointSet missionPointSet;
    
    public EscortForPlayerWaypointFactory(IFlight escortedFlight, IFlight escortFlight)
    {
        this.escortedFlight = escortedFlight;
        this.escortFlight = escortFlight;
    }

    public IMissionPointSet createWaypoints() throws PWCGException
    {
        missionPointSet = new MissionPointEscortWaypointSet(escortFlight);
        
        McuWaypoint ingressWaypoint = createIngressWaypoint();
        missionPointSet.addWaypointBefore(ingressWaypoint);
        
        McuWaypoint rtbWP = ReturnToBaseWaypoint.createReturnToBaseWaypoint(escortFlight);
        missionPointSet.addWaypointAfter(rtbWP);
        
        EscortMcuSequence escortSequence = new EscortMcuSequence(escortedFlight, escortFlight);
        escortSequence.createPointDefenseSequence();
        missionPointSet.setCoverSequence(escortSequence);

        return missionPointSet;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException
    {
        Coordinate ingresseCoords = getCoverPosition();
        Orientation orient = new Orientation();
        orient.setyOri(escortFlight.getFlightData().getFlightInformation().getDepartureAirfield().getOrientation().getyOri());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.START_AREA);
        ingressWP.setSpeed(escortFlight.getFlightData().getFlightPlanes().getFlightCruisingSpeed());
        ingressWP.setPosition(ingresseCoords);
        ingressWP.setOrientation(orient);
        return ingressWP;
    }

    private Coordinate getCoverPosition()
    {
        McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = ingressWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        return coverPosition;
    }
}
