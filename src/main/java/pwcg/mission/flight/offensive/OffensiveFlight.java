package pwcg.mission.flight.offensive;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveFlight extends Flight
{
    private OffensiveFlightTypes offensiveFlightType =  OffensiveFlightTypes.OFFENSIVE_FLIGHT_FRONT;

    public static enum OffensiveFlightTypes
    {
        OFFENSIVE_FLIGHT_FRONT,
        OFFENSIVE_FLIGHT_TRANSPORT,
        OFFENSIVE_FLIGHT_AIRFIELD,
    }

    public OffensiveFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        createOffensiveFlightType();

	    OffensiveWaypoints waypoints = null;

	    if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT)
	    {
	        waypoints = new OffensiveWaypointsTransport(startPosition, getTargetCoords(), this, mission);
	    }
	    else if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD)
	    {
	        waypoints = new OffensiveWaypointsAirfield(startPosition, getTargetCoords(), this, mission);
	    }
	    else
	    {
	        waypoints = new OffensiveWaypointsFront(startPosition, getTargetCoords(), this, mission);
	    }

	    return waypoints.createWaypoints();
	}

    private void createOffensiveFlightType()
    {
        int roll = RandomNumberGenerator.getRandom(100);

        if (roll < 45)
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT;
        }
        else if (roll < 55)
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD;
        }
        else
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_FRONT;
        }
    }

    public OffensiveFlightTypes getOffensiveFlightType()
    {
        return offensiveFlightType;
    }

    public String getMissionObjective() throws PWCGException 
    {
        String objective = "";

        String objectiveName =  formMissionObjectiveLocation(getTargetCoords().copy());
        if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT)
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace at the specified transport hubs" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified transport hubs" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        else if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD)
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace at the specified airfields" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified airfields" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        else
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified front location" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        
        return objective;
    }
    
    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
