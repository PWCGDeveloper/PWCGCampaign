package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortForPlayerWaypointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

public class EscortForPlayerWaypointFactory
{
    private IFlight escortFlight;
    private IFlight playerFlightThatNeedsEscort;
    private MissionPointEscortForPlayerWaypointSet missionPointSet;
    
    public EscortForPlayerWaypointFactory(IFlight escortFlight, IFlight playerFlightThatNeedsEscort)
    {
        this.escortFlight = escortFlight;
        this.playerFlightThatNeedsEscort = playerFlightThatNeedsEscort;
    }

    public IMissionPointSet createWaypoints() throws PWCGException
    {
        missionPointSet = new MissionPointEscortForPlayerWaypointSet();
        
        McuWaypoint rendezvousWaypoint = createRendezvousWaypoint();
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(playerFlightThatNeedsEscort, AirStartPattern.AIR_START_NEAR_WAYPOINT, rendezvousWaypoint);
        missionPointSet.addWaypointBefore(airStartWaypoint);
        missionPointSet.addWaypointBefore(rendezvousWaypoint);

        EscortMcuSequence escortSequence = new EscortMcuSequence(playerFlightThatNeedsEscort, escortFlight);
        escortSequence.createEscortSequence();
        missionPointSet.setEscortSequence(escortSequence);
       
        McuWaypoint rtbWP = ReturnToBaseWaypoint.createReturnToBaseWaypoint(escortFlight);
        missionPointSet.addWaypointAfter(rtbWP);

        return missionPointSet;
    }
    
    
    private McuWaypoint createRendezvousWaypoint() throws PWCGException
    {
        McuWaypoint rendezvousWaypoint = playerFlightThatNeedsEscort.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        Coordinate escortRendezvousCoordinate = rendezvousWaypoint.getPosition();
        escortRendezvousCoordinate.setYPos(escortRendezvousCoordinate.getYPos() + 300.0);

        McuWaypoint escortRendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
        escortRendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
        escortRendezvousWaypoint.setPosition(escortRendezvousCoordinate);
        escortRendezvousWaypoint.setTargetWaypoint(true);
        escortRendezvousWaypoint.setSpeed(escortFlight.getFlightCruisingSpeed());
        return escortRendezvousWaypoint;
    }

}
