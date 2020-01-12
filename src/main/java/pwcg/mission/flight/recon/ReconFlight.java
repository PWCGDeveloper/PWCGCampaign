package pwcg.mission.flight.recon;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPayloadBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.FlightWaypointGroupFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuWaypoint;

public class ReconFlight extends Flight implements IFlight
{
    ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;
    
    public static enum ReconFlightTypes
    {
        RECON_FLIGHT_FRONT,
        RECON_FLIGHT_TRANSPORT,
        RECON_FLIGHT_AIRFIELD,
    }

    public ReconFlight(IFlightInformation flightInformation)
    {
        super(flightInformation);
    }

    public void createFlight() throws PWCGException
    {
        flightData.initialize(this);
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        setFlightPayload();
    }

    private void createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_FRONT, this);

        IMissionPointSet flightBegin = FlightWaypointGroupFactory.createFlightBegin(this, AirStartPattern.AIR_START_NEAR_AIRFIELD, ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_BEGIN, flightBegin);

        IMissionPointSet missionWaypoints = createReconWaypoints(ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_MISSION_PATROL, missionWaypoints);
        
        IMissionPointSet flightEnd = FlightWaypointGroupFactory.createFlightEndAtHomeField(this);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_END, flightEnd);
        
        
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }
    
    private IMissionPointSet createReconWaypoints(McuWaypoint ingressWaypoint) throws PWCGException 
	{	    
	    if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
	    {
	        ReconTransportWaypointsFactory waypoints = new ReconTransportWaypointsFactory(this);
            return waypoints.createWaypoints(ingressWaypoint);
	    }
	    else if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
        {
	        ReconAirfieldWaypointsFactory waypoints = new ReconAirfieldWaypointsFactory(this);
            return waypoints.createWaypoints(ingressWaypoint);
        }
	    else
	    {
	        ReconFrontWaypointsFactory waypoints = new ReconFrontWaypointsFactory(this);
	        return waypoints.createWaypoints(ingressWaypoint);
	    }		
	}

    public ReconFlightTypes getReconFlightType()
    {
        return reconFlightType;
    }

    @Override
    public void finalize() throws PWCGException
    {
        flightData.getWaypointPackage().finalize();
    }
}
