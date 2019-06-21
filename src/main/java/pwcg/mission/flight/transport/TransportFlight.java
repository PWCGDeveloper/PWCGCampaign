package pwcg.mission.flight.transport;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.LandingBuilder;
import pwcg.mission.mcu.McuWaypoint;

public class TransportFlight extends Flight
{
    private IAirfield arrivalAirfield = null;

    public TransportFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	public void createUnitMission() throws PWCGException  
	{
	    determineTargetAirfield();
		super.createUnitMission();
	}

	private void determineTargetAirfield() throws PWCGException
    {
	    AirfieldManager airfieldManager = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager();
	    arrivalAirfield = airfieldManager.getAirfieldFinder().findClosestAirfieldForSide(getTargetCoords(), getCampaign().getDate(), getSquadron().getCountry().getSide());    
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        TransportWaypoints waypointGenerator = new TransportWaypoints(
                getSquadron().determineCurrentAirfieldCurrentMap(getCampaign().getDate()), 
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

    @Override
    protected void createLanding() throws PWCGException, PWCGException
    {
        LandingBuilder landingBuilder = new LandingBuilder(flightInformation.getCampaign());
        landing = landingBuilder.createLanding(arrivalAirfield);
    }
}
