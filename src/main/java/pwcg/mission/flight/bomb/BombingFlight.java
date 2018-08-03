package pwcg.mission.flight.bomb;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.mcu.McuWaypoint;

public class BombingFlight extends GroundTargetAttackFlight
{
    static private int BOMB_ATTACK_TIME = 180;

	protected BombingAltitudeLevel bombingAltitudeLevel = BombingAltitudeLevel.MED;

	public BombingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit, BOMB_ATTACK_TIME);
    }

	public void createUnitMission() throws PWCGException  
	{
		super.createUnitMission();
		super.createAttackArea(this.getMaximumFlightAltitude());
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		if (getFlightType() == FlightTypes.STRATEGIC_BOMB)
		{
		    if(getSquadron().determineIsNightSquadron())
		    {
		        return 1;
		    }
		}
		
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();
		
		int BombingMinimum = configManager.getIntConfigParam(ConfigItemKeys.BombingMinimumKey);
		int BombingAdditional = configManager.getIntConfigParam(ConfigItemKeys.BombingAdditionalKey) + 1;
		numPlanesInFlight = BombingMinimum + RandomNumberGenerator.getRandom(BombingAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        BombingWaypoints waypointGenerator = new BombingWaypoints(
                startPosition, 
                getTargetCoords(), 
                this,
                mission,
                bombingAltitudeLevel);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();

	    return waypointList;
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
	}

	public BombingAltitudeLevel getBombingAltitude() 
	{
		return bombingAltitudeLevel;
	}

	public void setBombingAltitudeLevel(BombingAltitudeLevel bombingAltitudeLevel) 
	{
		this.bombingAltitudeLevel = bombingAltitudeLevel;
	}
}
