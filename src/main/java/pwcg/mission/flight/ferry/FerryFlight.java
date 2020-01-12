package pwcg.mission.flight.ferry;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
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

public class FerryFlight extends Flight implements IFlight
{
    public FerryFlight(IFlightInformation flightInformation)
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
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_TARGET, this);

        IFlightInformation flightInformation = flightData.getFlightInformation();
        
        IMissionPointSet flightBegin = FlightWaypointGroupFactory.createFlightBegin(this, AirStartPattern.AIR_START_NEAR_AIRFIELD, ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_BEGIN, flightBegin);

        IAirfield fromAirfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(flightInformation.getCampaign().getSquadronMoveEvent().getLastAirfield());
        IAirfield toAirfield = flightInformation.getSquadron().determineCurrentAirfieldCurrentMap(flightInformation.getCampaign().getDate());
        FerryWaypointFactory missionWaypointFactory = new FerryWaypointFactory(this, fromAirfield, toAirfield);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_MISSION_PATROL, missionWaypoints);
        
        IMissionPointSet flightEnd = FlightWaypointGroupFactory.createFlightEndAtHomeField(this);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_END, flightEnd);
        
        
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    @Override
    public void finalize() throws PWCGException
    {
        flightData.getWaypointPackage().finalize();
    }
}
