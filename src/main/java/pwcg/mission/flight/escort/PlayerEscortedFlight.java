package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPayloadBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortedFlight extends Flight implements IFlight
{          
    public PlayerEscortedFlight(IFlightInformation flightInformation)
    {
        super (flightInformation);
    }

    public void createFlight() throws PWCGException
    {
        initialize(this);
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        WaypointPriority.setWaypointsNonFighterPriority(this);
        setFlightPayload();
    }

    @Override
    public IMissionPointSet createFlightSpecificWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        PlayerEscortedWaypointFactory missionWaypointFactory = new PlayerEscortedWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        return missionWaypoints;
    }

    private void createWaypoints() throws PWCGException
    {        
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_TARGET, this);

        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(this);
        this.getWaypointPackage().addMissionPointSet(flightActivate);

        RendezvousWaypointBuilder rendezvousWaypointBuilder = new RendezvousWaypointBuilder(this, ingressWaypoint);
        IMissionPointSet flightRendezvous = rendezvousWaypointBuilder.createFlightRendezvous();
        flightRendezvous.disableLinkToNextTarget();
        this.getWaypointPackage().addMissionPointSet(flightRendezvous);

        IMissionPointSet missionWaypoints = createFlightSpecificWaypoints(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }


    @Override
    public void finalizeFlight() throws PWCGException
    {
        super.finalizeFlight();
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }
}
