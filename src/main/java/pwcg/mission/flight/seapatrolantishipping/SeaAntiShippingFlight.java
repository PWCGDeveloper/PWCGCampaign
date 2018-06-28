package pwcg.mission.flight.seapatrolantishipping;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingFlight extends GroundTargetAttackFlight
{
    static private int SEA_ATTACK_ALT = 600;
    static private int SEA_ATTACK_TIME = 360;

	public SeaAntiShippingFlight() 
	{
		super (SEA_ATTACK_TIME);
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
				FlightTypes flightType,
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
        super.initialize (mission, campaign, FlightTypes.ANTI_SHIPPING, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	public void createUnitMission() throws PWCGException  
	{
        super.createUnitMission();
        super.createAttackArea(SEA_ATTACK_ALT);
	}

	@Override
	public int calcNumPlanes() 
	{
		numPlanesInFlight = 1 + RandomNumberGenerator.getRandom(2);
		
		return numPlanesInFlight;

	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaAntiShippingWaypoints waypointGenerator = new SeaAntiShippingWaypoints(
				startPosition, 
				targetCoords, 
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
