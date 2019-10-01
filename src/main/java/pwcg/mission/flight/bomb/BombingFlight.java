package pwcg.mission.flight.bomb;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class BombingFlight extends GroundTargetAttackFlight
{
    static private int BOMB_ATTACK_TIME = 180;

	public BombingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit, BOMB_ATTACK_TIME);
    }

	public void createUnitMission() throws PWCGException  
	{
		super.createUnitMission();
		super.createAttackArea(flightInformation.getAltitude());
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        BombingWaypoints waypointGenerator = new BombingWaypoints(this);
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
	    return waypointList;
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
	}
}
