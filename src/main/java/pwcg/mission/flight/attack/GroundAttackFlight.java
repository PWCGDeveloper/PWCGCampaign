package pwcg.mission.flight.attack;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackFlight extends GroundTargetAttackFlight
{
    static public int GROUND_ATTACK_ALT = 500;
    static public int GROUND_ATTACK_TIME = 360;
    	
    public GroundAttackFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit, GROUND_ATTACK_TIME);
    }

	public void createUnitMission() throws PWCGException  
	{
		super.createUnitMission();
        super.createAttackArea(GROUND_ATTACK_ALT);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		GroundAttackWaypoints waypointGenerator = new GroundAttackWaypoints(this);

		List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
	    
        return waypointList;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }
}
