package pwcg.mission.flight.paradrop;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class ParaDropFlight extends GroundTargetAttackFlight
{
    static private int DROP_TIME = 180;

	public ParaDropFlight() 
	{
		super (DROP_TIME);
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				FlightTypes flightType,
				Coordinate targetCoords, 
				Squadron squad, 
                MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

    @Override
	public void createUnitMission() throws PWCGException  
	{
		super.createUnitMission();
		super.createAttackArea(this.getMaximumFlightAltitude());
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
        int transportMinimum = 1;
        int transportAdditional = 4;
        numPlanesInFlight = transportMinimum + RandomNumberGenerator.getRandom(transportAdditional);        
        return numPlanesInFlight;
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        ParaDropWaypoints waypointGenerator = new ParaDropWaypoints(
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
	

    @Override
    public String getMissionObjective() throws PWCGMissionGenerationException, PWCGException
    {
        String objective = "Drop our paratroops" + formMissionObjectiveLocation(targetCoords.copy()) + ".";       
        if (flightType == FlightTypes.CARGO_DROP)
        {
            objective = "Perform a carrgo drop" + formMissionObjectiveLocation(targetCoords.copy()) + ".";     
        }
        
        return objective;
    }
}
