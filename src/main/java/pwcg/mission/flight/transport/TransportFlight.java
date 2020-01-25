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
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
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
        initialize(this);
        
        determineTargetAirfield();
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        WaypointPriority.setWaypointsNonFighterPriority(this);
    }

    @Override
    public IMissionPointSet createFlightSpecificWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        TransportWaypointFactory missionWaypointFactory = new TransportWaypointFactory(this, this.getFlightInformation().getAirfield(), arrivalAirfield);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        return missionWaypoints;
    }

    private void createWaypoints() throws PWCGException
    {
        MissionPointSetFactory.createStandardMissionPointSet(this, AirStartPattern.AIR_START_NEAR_AIRFIELD, IngressWaypointPattern.INGRESS_NEAR_FRONT);
    }
 
   	private void determineTargetAirfield() throws PWCGException
    {
	    AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
	    arrivalAirfield = airfieldManager.getAirfieldFinder().findClosestAirfieldForSide(
	            this.getFlightInformation().getTargetPosition(), getCampaign().getDate(), this.getSquadron().getCountry().getSide());    
    }

    public IAirfield getArrivalAirfield()
    {
        return arrivalAirfield;
    }
}
