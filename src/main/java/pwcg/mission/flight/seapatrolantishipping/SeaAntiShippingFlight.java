package pwcg.mission.flight.seapatrolantishipping;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.divebomb.DiveBombingFlight;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingFlight extends GroundTargetAttackFlight
{
    static private int SEA_ATTACK_TIME = 180;

    
    public SeaAntiShippingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit, SEA_ATTACK_TIME);
    }

	public void createUnitMission() throws PWCGException  
	{
        super.createUnitMission();
        super.createAttackArea(getAttackAltitude());
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaAntiShippingWaypoints waypointGenerator = new SeaAntiShippingWaypoints(this);
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();        
        return waypointList;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }
	
	private int getAttackAltitude() throws PWCGException
	{
	    if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB)
	    {
	        return flightInformation.getAltitude();
	    }
	    else if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            return DiveBombingFlight.DIVE_BOMB_ALT;
        }
	    else if (flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK)
        {
	        return GroundAttackFlight.GROUND_ATTACK_ALT;
        }
	    else
	        
	    {
	        throw new PWCGException ("Unexpected anti shipping flight type " + flightInformation.getFlightType());
	    }
	}

}
