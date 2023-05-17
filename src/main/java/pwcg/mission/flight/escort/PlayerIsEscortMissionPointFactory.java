package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortForAiWaypointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

public class PlayerIsEscortMissionPointFactory
{
    private IFlight escortFlight;
    private IFlight escortedFlight;
    private MissionPointEscortForAiWaypointSet missionPointSet;

    public PlayerIsEscortMissionPointFactory(IFlight escortFlight, IFlight escortedFlight) throws PWCGException
    {
        this.escortFlight = escortFlight;
        this.escortedFlight = escortedFlight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet = new MissionPointEscortForAiWaypointSet();
        missionPointSet.addWaypointBefore(ingressWaypoint);
   
        McuWaypoint rendezvousWaypoint = createRendezvousWaypoint();
        missionPointSet.addWaypointBefore(rendezvousWaypoint);
               
        EscortMcuSequence escortSequence = new EscortMcuSequence(escortedFlight, escortFlight);
        escortSequence.createEscortSequence();
        missionPointSet.setCoverSequence(escortSequence);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(escortFlight, ingressWaypoint.getPosition());
        missionPointSet.addWaypointAfter(egressWaypoint);

        return missionPointSet;
    }    

	private McuWaypoint createRendezvousWaypoint() throws PWCGException  
	{
		MissionPoint rendezvous = escortedFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
		Coordinate coord = rendezvous.getPosition().copy();
		coord.setYPos(escortFlight.getFlightInformation().getAltitude());

		McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
		rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);		
		rendezvousWaypoint.setPosition(coord);	
		rendezvousWaypoint.setTargetWaypoint(true);
		rendezvousWaypoint.setSpeed(escortFlight.getFlightCruisingSpeed());
		return rendezvousWaypoint;
	}
}
