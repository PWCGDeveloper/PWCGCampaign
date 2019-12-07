package pwcg.mission.flight.recon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class ReconFlight extends Flight
{
    ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;
    
    public static enum ReconFlightTypes
    {
        RECON_FLIGHT_FRONT,
        RECON_FLIGHT_TRANSPORT,
        RECON_FLIGHT_AIRFIELD,
    }
    
    public ReconFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
	    ReconWaypoints waypoints = null;
	    
	    if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
	    {
	        waypoints = new ReconWaypointsTransport(this);
	    }
	    else if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
        {
	        waypoints = new ReconWaypointsAirfield(this);
        }
	    else
	    {
            waypoints = new ReconWaypointsFront(this);
	    }
		
		return waypoints.createWaypoints();
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

    public ReconFlightTypes getReconFlightType()
    {
        return reconFlightType;
    }

    public void setReconFlightType(ReconFlightTypes reconFlightType)
    {
        this.reconFlightType = reconFlightType;
    }

}
