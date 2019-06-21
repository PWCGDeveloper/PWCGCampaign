package pwcg.mission.flight.seapatrolantishipping;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingFlight extends GroundTargetAttackFlight
{
    static private int SEA_ATTACK_ALT = 600;
    static private int SEA_ATTACK_TIME = 360;

    
    public SeaAntiShippingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit, SEA_ATTACK_TIME);
    }

	public void createUnitMission() throws PWCGException  
	{
        super.createUnitMission();
        super.createAttackArea(SEA_ATTACK_ALT);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaAntiShippingWaypoints waypointGenerator = new SeaAntiShippingWaypoints(
				startPosition, 
				getTargetCoords(), 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }

	public String getMissionObjective() 
	{
	    String objective = "Patrol sea lanes on the specified route.  " + 
	                    "Engage any enemy shipping that you encounter";

	    return objective;
	}

}
