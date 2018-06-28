package pwcg.mission.flight.transport;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class TransportFlight extends Flight
{
	public TransportFlight() 
	{
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				FlightTypes flightType,
				Coordinate targetCoords, 
				Squadron squadron, 
                MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squadron, missionBeginUnit, isPlayerFlight);
	}

	public void createUnitMission() throws PWCGException  
	{
	    determineTargetAirfield();
		super.createUnitMission();
	}

	private void determineTargetAirfield() throws PWCGException
    {
	    AirfieldManager airfieldManager = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager();
	    arrivalAirfield = airfieldManager.getAirfieldFinder().findClosestAirfieldForSide(targetCoords, campaign.getDate(), squadron.getCountry().getSide());    
    }

    @Override
	public int calcNumPlanes() throws PWCGException 
	{
		int transportMinimum = 1;
		int transportAdditional = 4;
		numPlanesInFlight = transportMinimum + RandomNumberGenerator.getRandom(transportAdditional);		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        TransportWaypoints waypointGenerator = new TransportWaypoints(
                squadron.determineCurrentAirfieldCurrentMap(campaign.getDate()), 
                arrivalAirfield, 
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
        String objective = "Transport supplies to the airfield at " + arrivalAirfield.getName() + ".";
        return objective;
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
