package pwcg.mission.flight.balloondefense;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointPointDefenseWaypointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.PointDefenseMcuSequence;

public class BalloonDefenseWaypointFactory
{
    private IFlight flight;
    private MissionPointPointDefenseWaypointSet missionPointSet;

    public BalloonDefenseWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
        this.missionPointSet = new MissionPointPointDefenseWaypointSet(flight);
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint balloonDefenseWP = createTargetWaypoints();
        missionPointSet.addWaypointBefore(balloonDefenseWP);
        
        PointDefenseMcuSequence coverSequence = new PointDefenseMcuSequence(flight);
        coverSequence.createPointDefenseSequence();
        missionPointSet.setCoverSequence(coverSequence);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypointAfter(egressWaypoint);

        return missionPointSet;
    }

	protected McuWaypoint createTargetWaypoints() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(flight.getFlightData().getFlightInformation().getTargetPosition().getXPos() + 50.0);
		coord.setZPos(flight.getFlightData().getFlightInformation().getTargetPosition().getZPos() + 50.0);
		coord.setYPos(flight.getFlightData().getFlightInformation().getAltitude());

		McuWaypoint balloonDefenseWP = WaypointFactory.createBalloonDefenseWaypointType();		
		balloonDefenseWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonDefenseWP.setSpeed(flight.getFlightData().getFlightPlanes().getFlightCruisingSpeed());
		balloonDefenseWP.setPosition(coord);	
		balloonDefenseWP.setTargetWaypoint(true);
        return balloonDefenseWP;
	}
}
