package pwcg.mission.flight.transport;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.FlightWaypointGroupFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuWaypoint;

public class TransportFlight extends Flight implements IFlight
{
    private IAirfield arrivalAirfield = null;

    public TransportFlight(IFlightInformation flightInformation)
    {
        super (flightInformation);
    }

    public void createFlight() throws PWCGException
    {
        flightData.initialize(this);
        
        determineTargetAirfield();
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        WaypointPriority.setWaypointsNonFighterPriority(this);
    }
    
    private void createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_TARGET, this);

        IMissionPointSet flightBegin = FlightWaypointGroupFactory.createFlightBegin(this, AirStartPattern.AIR_START_NEAR_AIRFIELD, ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_BEGIN, flightBegin);

        TransportWaypointFactory missionWaypointFactory = new TransportWaypointFactory(this, flightData.getFlightInformation().getAirfield(), arrivalAirfield);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_MISSION_ATTACK, missionWaypoints);
        
        IMissionPointSet flightEnd = FlightWaypointGroupFactory.createFlightEnd(this, arrivalAirfield);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_END, flightEnd);        
    }
 
   	private void determineTargetAirfield() throws PWCGException
    {
	    AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
	    arrivalAirfield = airfieldManager.getAirfieldFinder().findClosestAirfieldForSide(getTargetPosition(), getCampaign().getDate(), flightData.getFlightInformation().getSquadron().getCountry().getSide());    
    }

    public IAirfield getArrivalAirfield()
    {
        return arrivalAirfield;
    }
}
